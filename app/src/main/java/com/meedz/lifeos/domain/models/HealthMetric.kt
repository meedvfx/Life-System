package com.meedz.lifeos.domain.models

data class HealthMetric(
    val id: Long = 0,
    val type: HealthMetricType,
    val value: Double,
    val unit: String,
    val dateMillis: Long = System.currentTimeMillis()
)

enum class HealthMetricType {
    WEIGHT, SLEEP_HOURS, WORKOUT_MINUTES, WATER_GLASSES
}
