package com.batchtrace.app.viewmodel

import com.batchtrace.app.data.model.User

sealed class AuthUiState {

    data object Idle : AuthUiState()

    data object Loading : AuthUiState()

    data class Success(
        val user: User
    ) : AuthUiState()

    data class Error(
        val message: String
    ) : AuthUiState()
}