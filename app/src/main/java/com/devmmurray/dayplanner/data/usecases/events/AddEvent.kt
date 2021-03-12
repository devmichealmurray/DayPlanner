package com.devmmurray.dayplanner.data.usecases.events

import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.data.repository.dbrepos.EventsRepository

class AddEvent(private val eventsRepository: EventsRepository) {
    suspend operator fun invoke(event: EventEntity) =
        eventsRepository.addEvent(event)
}

class GetEvents(private val eventsRepository: EventsRepository) {
    operator fun invoke(dateId: String) =
        eventsRepository.getEvents(dateId)
}

class GetAllEvents(private val eventsRepository: EventsRepository) {
    operator fun invoke() =
        eventsRepository.getAllEvents()
}

class GetEventById(private val eventsRepository: EventsRepository) {
    operator fun invoke(id: Long) =
        eventsRepository.getEventById(id)
}

class DeleteEvent(private val eventsRepository: EventsRepository) {
    suspend operator fun invoke(id: Long) =
        eventsRepository.deleteEvent(id)
}