package com.meedz.lifeos.domain.models

data class Goal(
    val id: Long = 0,
    val title: String,
    val description: String,
    val targetDate: Long?,
    val isCompleted: Boolean = false,
    val progress: Int = 0
)
