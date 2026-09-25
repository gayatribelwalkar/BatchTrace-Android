package com.batchtrace.app.viewmodel

import androidx.lifecycle.ViewModel
import com.batchtrace.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _authState =
        MutableStateFlow<AuthUiState>(AuthUiState.Idle)

    val authState: StateFlow<AuthUiState> =
        _authState.asStateFlow()

    fun login(
        email: String,
        password: String
    ) {
        _authState.value = AuthUiState.Loading

        authRepository.login(
            email = email,
            password = password,

            onSuccess = { user ->
                _authState.value =
                    AuthUiState.Success(user)
            },

            onError = { message ->
                _authState.value =
                    AuthUiState.Error(message)
            }
        )
    }

    fun logout() {
        authRepository.logout()
        _authState.value = AuthUiState.Idle
    }

    fun clearError() {
        if (_authState.value is AuthUiState.Error) {
            _authState.value = AuthUiState.Idle
        }
    }
}