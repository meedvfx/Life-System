package com.meedz.lifeos.domain.models

data class Note(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val dateModified: Long = System.currentTimeMillis(),
    val tags: List<String> = emptyList(),
    val linkedNoteIds: List<String> = emptyList() // Zettelkasten bidirectional links
)
