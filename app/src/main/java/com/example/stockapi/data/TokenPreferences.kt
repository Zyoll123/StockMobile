package com.example.stockapi.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object TokenPreferences {
    private const val TOKEN_KEY = "auth_token"

    private val Context.dataStore by preferencesDataStore(name = "auth_pref")

    private val tokenKey = stringPreferencesKey(TOKEN_KEY)

    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    suspend fun getToken(context: Context): String? {
        val preferences = context.dataStore.data.first()
        return preferences[tokenKey]
    }

    suspend fun clearToken(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(tokenKey)
        }
    }
}