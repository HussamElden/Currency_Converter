package com.example.currencyconverter.entities

data class Query(
    val amount: Float,
    val from: String,
    val to: String
)