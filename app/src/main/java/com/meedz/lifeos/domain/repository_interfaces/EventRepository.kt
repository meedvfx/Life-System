package com.meedz.lifeos.domain.repository_interfaces

import com.meedz.lifeos.domain.models.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEventsBetween(start: Long, end: Long): Flow<List<Event>>
    suspend fun saveEvent(event: Event)
}
