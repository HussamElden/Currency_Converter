package com.example.currencyconverter.entities

data class Conversion(
    val date: String,
    val info: Info,
    val query: Query,
    val result: Double,
    val success: Boolean
)