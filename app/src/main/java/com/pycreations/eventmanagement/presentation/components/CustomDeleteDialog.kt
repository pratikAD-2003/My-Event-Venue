package com.pycreations.eventmanagement.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.pycreations.eventmanagement.R
import com.pycreations.eventmanagement.data.utils.Dimens

@Composable
fun CustomDeleteDialog(
    btnText : String,
    title: String,
    description: String,
    setShowDialog: (Boolean) -> Unit,
    result: (Boolean) -> Unit
) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(Dimens.PAD_10),
            color = colorResource(R.color.white).copy(0.8f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.PAD_20)
                ) {
                    MyText(text = title, fontWeight = FontWeight.Bold, fontSize = 20)
                    Spacer(Modifier.height(Dimens.PAD_5))
                    MyText(text = description, fontWeight = FontWeight.W400, fontSize = 18)
                    Spacer(Modifier.height(Dimens.PAD_10))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier.clickable {
                                result(false)
                            }
                        ) {
                            MyText(
                                text = "Cancel", fontWeight = FontWeight.SemiBold, fontSize = 16
                            )
                        }
                        Spacer(Modifier.width(Dimens.PAD_20))
                        Box(
                            modifier = Modifier.clickable {
                                result(true)
                            }
                        ) {
                            MyText(
                                text = btnText, fontWeight = FontWeight.SemiBold, fontSize = 16
                            )
                        }
                    }
                }
            }
        }
    }
}