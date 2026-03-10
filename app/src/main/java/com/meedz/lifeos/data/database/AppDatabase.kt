package com.meedz.lifeos.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meedz.lifeos.data.dao.EventDao
import com.meedz.lifeos.data.dao.GoalDao
import com.meedz.lifeos.data.dao.HabitDao
import com.meedz.lifeos.data.dao.TaskDao
import com.meedz.lifeos.data.entities.EventEntity
import com.meedz.lifeos.data.entities.GoalEntity
import com.meedz.lifeos.data.entities.HabitEntity
import com.meedz.lifeos.data.entities.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        HabitEntity::class,
        EventEntity::class,
        GoalEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun habitDao(): HabitDao
    abstract fun eventDao(): EventDao
    abstract fun goalDao(): GoalDao
}
