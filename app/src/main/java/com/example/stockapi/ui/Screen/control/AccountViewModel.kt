package com.example.stockapi.ui.Screen.control

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapi.data.AuthManager
import com.example.stockapi.data.TokenPreferences
import com.example.stockapi.data.model.LoginResponse
import com.example.stockapi.data.remote.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountViewModel: ViewModel() {
    private val repository = AccountRepository()

    private val _uiState = MutableStateFlow<AccountUiState>(AccountUiState.Idle)
    val uiState: StateFlow<AccountUiState> = _uiState

    fun login(context: Context, username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AccountUiState.Loading
            try {
                val response = repository.login(username, password)
                TokenPreferences.saveToken(context, response.token)
                _uiState.value = AccountUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = AccountUiState.Error(e.message ?: "Login failed")
            }
        }
    }

    suspend fun isLoggedIn(context: Context): Boolean {
        val token = TokenPreferences.getToken(context)
        return token != null
    }

}

sealed class AccountUiState {
    object Idle: AccountUiState()
    object Loading: AccountUiState()
    data class Success(val response: LoginResponse): AccountUiState()
    data class Error(val message: String): AccountUiState()
}