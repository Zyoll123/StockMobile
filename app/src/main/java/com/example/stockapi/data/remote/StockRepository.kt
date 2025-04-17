package com.example.stockapi.data.remote

import android.util.Log
import com.example.stockapi.data.AuthManager
import com.example.stockapi.data.KtorClient
import com.example.stockapi.data.model.Stock
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class StockRepository {
    suspend fun getAllStock(): List<Stock> {
        val token = AuthManager.getToken()

        return try {
            KtorClient.client.get("stock"){
                header(HttpHeaders.Authorization, "Bearer $token" )
            }.body()
        } catch (e: Exception) {
            Log.e("Api Error", "Get Failed", e)
            throw e
        }
    }
}