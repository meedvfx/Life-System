package com.meedz.lifeos.domain.models

data class Project(
    val id: Long = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val progress: Int = 0,
    val deadLine: Long? = null
)
