package mk.ukim.finki.internshipmanagement.infrastructure.messaging


interface EventMessagingRepository {
    fun send(topic: String, key: String, payload: String)
}

