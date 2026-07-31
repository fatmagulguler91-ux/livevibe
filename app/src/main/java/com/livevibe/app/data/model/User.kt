package com.livevibe.app.data.model

/**
 * Firestore `users/{userId}` dökümanına karşılık gelen model.
 *
 * NOT: coinBalance bilerek burada tutuluyor ama bu alan İSTEMCİ tarafından
 * asla direkt yazılmamalı. Firestore Security Rules'da bu alana client
 * yazma izni kapatılacak; değişiklikler yalnızca Cloud Functions üzerinden
 * (örn. hediye gönderme, coin satın alma doğrulaması) yapılacak.
 */
data class User(
    val uid: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    val bio: String = "",
    val age: Int? = null,
    val gender: String? = null,
    val email: String = "",
    val coinBalance: Long = 0L,
    val status: UserStatus = UserStatus.OFFLINE,
    val fcmToken: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class UserStatus {
    ONLINE, OFFLINE, AWAY
}
