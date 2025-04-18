package com.example.stockapi.data.remote

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.example.stockapi.data.AuthManager
import com.example.stockapi.data.KtorClient
import com.example.stockapi.data.TokenPreferences
import com.example.stockapi.data.model.Comment
import com.example.stockapi.data.model.CreateComment
import com.example.stockapi.data.model.PutComment
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class CommentRepository(private val context: Context) {
    suspend fun getCommentByStockId(stockId: Int): List<Comment> {
        val token = TokenPreferences.getToken(context)

        return try {
            KtorClient.client.get("comment/by-stockId/$stockId"){
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            Log.e("API Error", "failed get comment by stock id", e)
            throw e
        }
    }

    suspend fun addComment(title: String, content: String, stockId: Int): Comment {
        val token = TokenPreferences.getToken(context)

        val response = KtorClient.client.post("comment/$stockId"){
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(CreateComment(title, content, stockId))
        }

        val addCommentResponse = response.body<Comment>()
        return addCommentResponse
    }

    suspend fun putComment(title: String, content: String, id: Int): Comment {
        val token = TokenPreferences.getToken(context)

        val response = KtorClient.client.put("comment/$id") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(PutComment(title, content))
        }

        val putCommentResponse = response.body<Comment>()
        return putCommentResponse
    }
}