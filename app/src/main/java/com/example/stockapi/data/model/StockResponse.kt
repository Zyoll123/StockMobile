package com.example.stockapi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val id: Int,
    val symbol: String,
    val companyName: String,
    val purchase: Double,
    val lastDiv: Double,
    val industry: String,
    val marketCap: Long,
    val comments: List<Comment>
)