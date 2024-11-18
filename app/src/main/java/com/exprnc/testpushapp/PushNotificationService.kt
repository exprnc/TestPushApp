package com.exprnc.testpushapp

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveTokenToFirestore(token)
    }

    private fun saveTokenToFirestore(token: String) {
        val firestore = FirebaseFirestore.getInstance()
        val fieldName = "token_${token.take(3)}"

        val tokenData = mapOf(
            fieldName to token
        )

        firestore.collection("users")
            .document("tokens")
            .set(tokenData, SetOptions.merge())
            .addOnSuccessListener {
                println("Token saved successfully.")
            }
            .addOnFailureListener { e ->
                println("Error saving token: ${e.message}")
            }
    }
}