My Event Venue – All-in-One Event Manager (Admin & User)

📝 Description:
My Event Venue is a modern, feature-rich Android application built to streamline event creation, discovery, and ticketing for both organizers (admins) and attendees (users).
Users can discover nearby events using Mapbox Place AutoComplete, filter by category, location, or event name, and book free or paid tickets via Razorpay. Booked events appear under "Upcoming" or "Past" sections, and users receive instant confirmation notifications through Firebase Cloud Messaging.
Admins have a powerful dashboard to create events, manage them, and view real-time statistics including total revenue, sales, and ticket counts. Each event supports image uploads, live registration tracking, and even refund handling for paid events.
With secure Google & Email authentication, responsive Jetpack Compose UI, and offline capabilities via SharedPreferences, My Event Venue is a complete Firebase-powered solution for modern event management.

🛠️ Used Skills & Technologies:
🔐 Authentication:
Firebase Authentication (Email/Password & Google Sign-In)

🔔 Notifications:
Firebase Cloud Messaging (Event booking, cancellation, confirmation alerts)

🧠 Architecture:
MVVM (Model-View-ViewModel)
ViewModel + StateFlow + Repository pattern

🌍 Location & Places:
Mapbox Places Autocomplete API for location search & autofill

🖼️ Storage & Data:
Firebase Firestore (User, Event, Ticket, Stats)
Firebase Storage (Event image uploads)
SharedPreferences for local storage (user session, favorites)

🎫 Features:
Free & Paid ticket support (with Razorpay)
Favorite & bookmark events
Event filters (Upcoming / Past / Location / Category)
Profile editing & refund requests
Admin event statistics dashboard
Add, edit, delete event controls
Ticket booking & QR-based ID support (optional)

💄 UI & UX:
Jetpack Compose
Material You Design
Dark/Light mode ready

💰 Payment Gateway:
Razorpay payment integration (with ticket ID generation)
