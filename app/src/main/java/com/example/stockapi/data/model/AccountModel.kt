package com.example.stockapi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Register(
    val username: String,
    val email: String,
    val password: String
)

@Serializable
data class Login(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val username: String,
    val email: String,
    val token: String
)