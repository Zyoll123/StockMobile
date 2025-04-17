package com.example.stockapi.data.remote

import com.example.stockapi.data.AuthManager
import com.example.stockapi.data.KtorClient
import com.example.stockapi.data.model.Login
import com.example.stockapi.data.model.LoginResponse
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AccountRepository {
    suspend fun login(username: String, password: String): LoginResponse {
        val response = KtorClient.client.post("account/login") {
            contentType(ContentType.Application.Json)
            setBody(Login(username, password))
        }

        val loginResponse = response.body<LoginResponse>()
        return loginResponse
    }
}