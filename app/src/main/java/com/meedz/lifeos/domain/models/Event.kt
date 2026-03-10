package com.meedz.lifeos.domain.models

data class Event(
    val id: Long = 0,
    val title: String,
    val description: String,
    val startTime: Long,
    val endTime: Long,
    val type: String,
    val relatedId: Long?
)
