package com.meedz.lifeos.domain.models

data class ProductivityMetrics(
    val dateMillis: Long,
    val taskCompletionRate: Float, // 0 to 1
    val habitAdherenceRate: Float, // 0 to 1
    val overallScore: Int // 0 to 100
)
