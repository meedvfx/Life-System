package com.meedz.lifeos.domain.models

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val category: String,
    val dateMillis: Long = System.currentTimeMillis(),
    val type: TransactionType
)

enum class TransactionType {
    INCOME, EXPENSE
}
