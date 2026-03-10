package com.meedz.lifeos.domain.models

data class Habit(
    val id: Long = 0,
    val name: String,
    val description: String,
    val frequency: String,
    val streak: Int = 0,
    val lastCompletedDate: Long? = null
)
