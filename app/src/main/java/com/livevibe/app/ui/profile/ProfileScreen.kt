package com.livevibe.app.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.livevibe.app.data.model.User

/**
 * Faz 1 için geçici/basit profil ekranı - giriş başarılı olduğunda
 * kullanıcıya temel bilgilerini gösterir. Fotoğraf/bio düzenleme,
 * coin bakiyesi gösterimi vb. sonraki adımlarda buraya eklenecek.
 */
@Composable
fun ProfileScreen(user: User) {
    Scaffold { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Hoş geldin, ${user.displayName}", style = MaterialTheme.typography.titleLarge)
            Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Coin bakiyesi: ${user.coinBalance}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
