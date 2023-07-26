package com.example.mywetherapp.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Message from: ${remoteMessage.from}")
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Token: $token")
    }

    companion object {
        private const val TAG = "PushNotificationService"
    }
}
