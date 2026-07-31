package com.livevibe.app.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.livevibe.app.data.model.User
import com.livevibe.app.data.model.UserStatus
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Google Sign-In akışını yönetir. Modern (2024+) Android'de Google artık
 * eski GoogleSignInClient yerine Credential Manager API'sini öneriyor -
 * bu yüzden o yaklaşımı kullandık; Play Console'da uygulamayı yayınlarken
 * ekstra bir "deprecated API" uyarısıyla karşılaşmazsın.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    /**
     * Google Sign-In akışını başlatır ve Firebase'de oturum açar.
     * webClientId: strings.xml'deki default_web_client_id (Firebase Console'dan alınacak)
     *
     * @return Başarılıysa User, hata varsa exception fırlatır (ViewModel'de try/catch ile yakalanır)
     */
    suspend fun signInWithGoogle(context: Context, webClientId: String): User {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // ilk girişte tüm Google hesaplarını göster
            .setServerClientId(webClientId)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(context, request)
        val credential = result.credential

        if (credential !is CustomCredential ||
            credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            throw IllegalStateException("Beklenmeyen kimlik bilgisi türü")
        }

        val googleIdTokenCredential = try {
            GoogleIdTokenCredential.createFrom(credential.data)
        } catch (e: GoogleIdTokenParsingException) {
            throw IllegalStateException("Google ID token ayrıştırılamadı: ${e.message}")
        }

        val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
        val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
        val firebaseUser = authResult.user ?: throw IllegalStateException("Firebase kullanıcısı oluşturulamadı")

        return syncUserToFirestore(
            uid = firebaseUser.uid,
            displayName = firebaseUser.displayName ?: googleIdTokenCredential.displayName ?: "Kullanıcı",
            email = firebaseUser.email ?: "",
            photoUrl = firebaseUser.photoUrl?.toString() ?: googleIdTokenCredential.profilePictureUri?.toString()
        )
    }

    /**
     * İlk girişte Firestore'da kullanıcı dökümanı yoksa oluşturur,
     * varsa mevcut halini döner (üzerine yazmaz - profil bilgilerini kaybetmemek için).
     */
    private suspend fun syncUserToFirestore(
        uid: String,
        displayName: String,
        email: String,
        photoUrl: String?
    ): User {
        val userDocRef = firestore.collection("users").document(uid)
        val snapshot = userDocRef.get().await()

        return if (snapshot.exists()) {
            snapshot.toObject(User::class.java) ?: throw IllegalStateException("Kullanıcı verisi okunamadı")
        } else {
            val newUser = User(
                uid = uid,
                displayName = displayName,
                email = email,
                photoUrl = photoUrl,
                coinBalance = 0L,
                status = UserStatus.ONLINE
            )
            userDocRef.set(newUser).await()
            newUser
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}
