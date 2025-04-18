package com.example.stockapi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Int,
    val title: String,
    val content: String,
    val createdOn: String,
    val stockId: Int
)

@Serializable
data class CreateComment(
    val title: String,
    val content: String,
    val stockId: Int
)

@Serializable
data class PutComment(
    val title: String,
    val content: String
)