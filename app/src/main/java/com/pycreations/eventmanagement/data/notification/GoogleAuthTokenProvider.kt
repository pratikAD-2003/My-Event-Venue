package com.pycreations.eventmanagement.data.notification
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.pycreations.eventmanagement.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

class GoogleAuthTokenProvider(private val context: Context) {

    suspend fun getAccessToken(): String {
        val serviceAccount = readServiceAccountJson()
        val clientEmail = serviceAccount.getString("client_email")
        val privateKey = serviceAccount.getString("private_key").replace("\\n", "\n")
        val jwt = createJWT(clientEmail, privateKey)
        return fetchAccessToken(jwt)
    }

    private fun readServiceAccountJson(): JSONObject {
        val inputStream = context.resources.openRawResource(R.raw.service_account)
        val jsonText = inputStream.bufferedReader().use { it.readText() }
        return JSONObject(jsonText)
    }

//    private suspend fun fetchAccessToken(jwt: String): String = withContext(Dispatchers.IO) {
//        val request = Request.Builder()
//            .url("https://oauth2.googleapis.com/token")
//            .post(
//                RequestBody.create(
////                MediaType.parse("application/x-www-form-urlencoded"),
//                    "application/json".toMediaType(),
//                    "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=$jwt"
//            ))
//            .build()
//
//        val client = OkHttpClient()
//        val response = client.newCall(request).execute()
//        val responseBody = response.body?.string() ?: throw IOException("Failed to fetch access token")
//        return@withContext JSONObject(responseBody).getString("access_token")
//    }

    private suspend fun fetchAccessToken(jwt: String): String = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://oauth2.googleapis.com/token")
            .post(
                RequestBody.create(
                    "application/x-www-form-urlencoded".toMediaType(),
                    "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=$jwt"
                )
            )
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: throw IOException("Failed to fetch access token")

        val json = JSONObject(responseBody)

        if (json.has("access_token")) {
            return@withContext json.getString("access_token")
        } else {
            // Optional: Log the actual error
            Log.e("TOKEN_ERROR", "Failed to get access token: $responseBody")
            throw IOException("No access_token in response: $responseBody")
        }
    }


    @SuppressLint("NewApi")
    private fun createJWT(clientEmail: String, privateKey: String): String {
        val header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"RS256\",\"typ\":\"JWT\"}".toByteArray())
        val payload = Base64.getUrlEncoder().encodeToString(
            """{
                "iss":"$clientEmail",
                "scope":"https://www.googleapis.com/auth/firebase.messaging",
                "aud":"https://oauth2.googleapis.com/token",
                "exp":${System.currentTimeMillis() / 1000 + 3600},
                "iat":${System.currentTimeMillis() / 1000}
            }""".toByteArray()
        )
        val data = "$header.$payload"
        val signature = signData(data, privateKey)
        return "$data.$signature"
    }

    @SuppressLint("NewApi")
    private fun signData(data: String, privateKey: String): String {
        val sanitizedPrivateKey = privateKey
            .replace("\\n", "\n") // Convert literal newlines to real newlines
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\s".toRegex(), "") // Remove any unnecessary whitespace
        val keyBytes = Base64.getDecoder().decode(sanitizedPrivateKey)

        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val privateKeyObject = keyFactory.generatePrivate(keySpec)

        val signature = Signature.getInstance("SHA256withRSA")
        signature.initSign(privateKeyObject)
        signature.update(data.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(signature.sign())
    }

}
