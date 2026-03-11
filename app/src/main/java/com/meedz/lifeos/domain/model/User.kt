package com.meedz.lifeos.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val joinDate: Long,
    val isPremium: Boolean = false,
    val syncEnabled: Boolean = false
)
