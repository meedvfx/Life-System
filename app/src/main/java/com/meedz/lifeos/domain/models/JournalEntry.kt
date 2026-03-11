package com.meedz.lifeos.domain.models

data class JournalEntry(
    val id: Long = 0,
    val date: Long,
    val content: String,
    val moodScore: Int = 3, // 1 to 5
    val energyScore: Int = 3, // 1 to 5
    val tags: List<String> = emptyList()
)
