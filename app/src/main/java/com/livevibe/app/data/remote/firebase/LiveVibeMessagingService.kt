package com.livevibe.app.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Faz 1 kapsamında sadece token yenilenmesini Firestore'a yazıyoruz.
 * Bildirim gösterimi (yeni mesaj geldiğinde) ve mesaj işleme mantığı
 * mesajlaşma ekranı yazılırken buraya eklenecek.
 */
class LiveVibeMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .update("fcmToken", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // TODO: Faz 2 - yeni mesaj/hediye bildirimlerini burada NotificationCompat ile göster
    }
}
