package com.meedz.lifeos.domain.models

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val dueDate: Long?,
    val isRecurring: Boolean = false,
    val projectId: Long? = null
)
