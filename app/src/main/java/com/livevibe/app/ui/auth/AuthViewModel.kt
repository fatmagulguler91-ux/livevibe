package com.livevibe.app.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livevibe.app.data.model.User
import com.livevibe.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Success(val user: User) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signInWithGoogle(context: Context, webClientId: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            try {
                val user = authRepository.signInWithGoogle(context, webClientId)
                _uiState.value = AuthUiState.Success(user)
            } catch (e: Exception) {
                // Kullanıcı Google hesap seçme ekranını iptal ettiğinde de
                // buraya düşer - "Error" olarak gösterip sessizce Idle'a
                // dönmek, kullanıcıyı gereksiz yere korkutmaz.
                _uiState.value = AuthUiState.Error(e.message ?: "Bilinmeyen hata")
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
