package com.meedz.lifeos.data.repository

import com.meedz.lifeos.data.dao.EventDao
import com.meedz.lifeos.data.mappers.toDomain
import com.meedz.lifeos.data.mappers.toEntity
import com.meedz.lifeos.domain.models.Event
import com.meedz.lifeos.domain.repository_interfaces.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao
) : EventRepository {
    override fun getEventsBetween(start: Long, end: Long): Flow<List<Event>> {
        return eventDao.getEventsBetween(start, end).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveEvent(event: Event) {
        eventDao.insertEvent(event.toEntity())
    }
}
