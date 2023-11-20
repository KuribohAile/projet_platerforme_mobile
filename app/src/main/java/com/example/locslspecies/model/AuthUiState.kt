package com.example.locslspecies.model

sealed class AuthUiState {
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
    object Loading : AuthUiState()
    object Empty : AuthUiState() // Initial state
}
