package com.example.wallet

data class MonthData(
    val monthName: String,
    val income: Double,
    val expense: Double,
    val limit: Double,
    val transactions: List<Transaction>
)