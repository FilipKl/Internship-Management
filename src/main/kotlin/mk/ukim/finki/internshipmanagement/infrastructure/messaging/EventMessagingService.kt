package mk.ukim.finki.internshipmanagement.infrastructure.messaging


interface EventMessagingService {
    fun send(topic: String, key: String, payload: String)
}

