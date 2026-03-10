package com.meedz.lifeos.data.mappers

import com.meedz.lifeos.data.entities.EventEntity
import com.meedz.lifeos.data.entities.GoalEntity
import com.meedz.lifeos.data.entities.HabitEntity
import com.meedz.lifeos.data.entities.TaskEntity
import com.meedz.lifeos.domain.models.Event
import com.meedz.lifeos.domain.models.Goal
import com.meedz.lifeos.domain.models.Habit
import com.meedz.lifeos.domain.models.Task

fun TaskEntity.toDomain() = Task(id, title, description, isCompleted, dueDate, isRecurring, projectId)
fun Task.toEntity() = TaskEntity(id, title, description, isCompleted, dueDate, isRecurring, projectId)

fun HabitEntity.toDomain() = Habit(id, name, description, frequency, streak, lastCompletedDate)
fun Habit.toEntity() = HabitEntity(id, name, description, frequency, streak, lastCompletedDate)

fun EventEntity.toDomain() = Event(id, title, description, startTime, endTime, type, relatedId)
fun Event.toEntity() = EventEntity(id, title, description, startTime, endTime, type, relatedId)

fun GoalEntity.toDomain() = Goal(id, title, description, targetDate, isCompleted, progress)
fun Goal.toEntity() = GoalEntity(id, title, description, targetDate, isCompleted, progress)
