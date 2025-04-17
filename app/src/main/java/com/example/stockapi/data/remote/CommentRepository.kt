package com.example.stockapi.data.remote

import android.util.Log
import com.example.stockapi.data.AuthManager
import com.example.stockapi.data.KtorClient
import com.example.stockapi.data.model.Comment
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class CommentRepository {
    suspend fun getCommentByStockId(stockId: Int): List<Comment> {
        val token = AuthManager.getToken()

        return try {
            KtorClient.client.get("comment/by-stockId/$stockId"){
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            Log.e("API Error", "failed get comment by stock id", e)
            throw e
        }
    }
}