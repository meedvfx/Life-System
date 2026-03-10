package com.meedz.lifeos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val targetDate: Long?,
    val isCompleted: Boolean = false,
    val progress: Int = 0
)
