package com.example.stockapi.data

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }

        install(HttpTimeout) {
            connectTimeoutMillis = 30000
            requestTimeoutMillis = 30000
            socketTimeoutMillis = 30000
        }

        defaultRequest {
            url("http://192.168.101.13:5295/api/")
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("KtorService", message)
                }
            }

            level = LogLevel.ALL

            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
    }
}