package mk.ukim.finki.internshipmanagement.infrastructure.messaging.impl

import mk.ukim.finki.internshipmanagement.infrastructure.messaging.EventMessagingRepository
import mk.ukim.finki.internshipmanagement.infrastructure.messaging.EventMessagingService
import org.springframework.stereotype.Service

/**
 * Service implementation that delegates to the repository layer.
 * This is a thin delegation layer - all actual work happens in the repository.
 */
@Service
class EventMessagingServiceImpl(
    val eventMessagingRepository: EventMessagingRepository
) : EventMessagingService {

    override fun send(topic: String, key: String, payload: String) {
        eventMessagingRepository.send(topic, key, payload)
    }
}

