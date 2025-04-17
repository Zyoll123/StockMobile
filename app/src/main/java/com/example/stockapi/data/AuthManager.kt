package com.example.stockapi.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

object AuthManager {
    private var _token = mutableStateOf<String?>(null)
    val tokenState: State<String?> = _token

    fun setToken(newToken: String) {
        _token.value = newToken
    }

    fun clearToke() {
        _token.value = null
    }

    fun getToken(): String {
        return _token.value ?: throw IllegalArgumentException("Token tidak tersedia")
    }
}