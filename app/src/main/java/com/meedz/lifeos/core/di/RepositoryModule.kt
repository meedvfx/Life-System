package com.meedz.lifeos.core.di

import com.meedz.lifeos.data.repository.EventRepositoryImpl
import com.meedz.lifeos.data.repository.GoalRepositoryImpl
import com.meedz.lifeos.data.repository.HabitRepositoryImpl
import com.meedz.lifeos.data.repository.TaskRepositoryImpl
import com.meedz.lifeos.domain.repository_interfaces.EventRepository
import com.meedz.lifeos.domain.repository_interfaces.GoalRepository
import com.meedz.lifeos.domain.repository_interfaces.HabitRepository
import com.meedz.lifeos.domain.repository_interfaces.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindHabitRepository(
        habitRepositoryImpl: HabitRepositoryImpl
    ): HabitRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(
        eventRepositoryImpl: EventRepositoryImpl
    ): EventRepository

    @Binds
    @Singleton
    abstract fun bindGoalRepository(
        goalRepositoryImpl: GoalRepositoryImpl
    ): GoalRepository
}
