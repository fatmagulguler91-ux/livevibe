package com.livevibe.app.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.livevibe.app.R
import com.livevibe.app.data.model.User

/**
 * Uygulama açılışında gösterilen tek ekran: sadece Google ile giriş.
 * Başarılı girişte onSignedIn callback'i ile ana navigasyona geçiş yapılır.
 */
@Composable
fun AuthScreen(
    onSignedIn: (User) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val webClientId = stringResource(id = R.string.default_web_client_id)

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onSignedIn((uiState as AuthUiState.Success).user)
        }
    }

    Scaffold { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineMedium
            )

            when (val state = uiState) {
                is AuthUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
                }

                is AuthUiState.Error -> {
                    Text(
                        text = stringResource(id = R.string.sign_in_error),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    SignInButton {
                        viewModel.signInWithGoogle(context, webClientId)
                    }
                }

                else -> {
                    SignInButton(
                        modifier = Modifier.padding(top = 32.dp)
                    ) {
                        viewModel.signInWithGoogle(context, webClientId)
                    }
                }
            }
        }
    }
}

@Composable
private fun SignInButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp)
    ) {
        Text(text = stringResource(id = R.string.sign_in_with_google))
    }
}
