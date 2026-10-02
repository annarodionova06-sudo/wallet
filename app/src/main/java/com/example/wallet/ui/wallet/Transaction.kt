package com.example.wallet

data class Transaction(
    val icon: String,
    val title: String,
    val date: String,
    val amount: Double,
    val isIncome: Boolean
)