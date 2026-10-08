package com.example.wallet

data class CategorySum(
    val categoryName: String,
    val icon: String,
    val totalAmount: Double
)

data class MonthStatistics(
    val monthYearTitle: String,
    val totalSpent: Double,
    val categories: List<CategorySum>
)