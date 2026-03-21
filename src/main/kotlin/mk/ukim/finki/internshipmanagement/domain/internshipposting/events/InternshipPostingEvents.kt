package mk.ukim.finki.internshipmanagement.domain.internshipposting.events

import mk.ukim.finki.internshipmanagement.domain.common.DomainEvent
import mk.ukim.finki.internshipmanagement.domain.internshipposting.Location
import mk.ukim.finki.internshipmanagement.domain.internshipposting.commands.*
import java.time.LocalDateTime
import java.util.*

abstract class InternshipPostingEvent(
    aggregateId: String,
    eventId: String = UUID.randomUUID().toString(),
    occurredAt: LocalDateTime = LocalDateTime.now()
) : DomainEvent(aggregateId, eventId, occurredAt)

data class InternshipPostingCreatedEvent(
    val aggId: String,
    val title: String,
    val company: String,
    val description: String,
    val techStack: String,
    val location: String
) : InternshipPostingEvent(aggId) {
    constructor(command: CreateInternshipPostingCommand) : this(
        command.id.id,
        command.title,
        command.company,
        command.description,
        command.techStack,
        command.location

    )
    override fun getEventType() = "InternshipPostingCreated"
}

data class InternshipPostingUpdatedEvent(
    val aggId: String,
    val title: String,
    val description: String,
    val techStack: String,
    val company: String,
    val location: String

) : InternshipPostingEvent(aggId) {
    constructor(command: UpdateInternshipPostingCommand) : this(
        command.id.id,
        command.title,
        command.description,
        command.techStack,
        command.company,
        command.location

    )
    override fun getEventType() = "InternshipPostingUpdated"
}

data class InternshipPostingEditedEvent(
    val aggId: String,
    val title: String,
    val description: String,
    val techStack: String
) : InternshipPostingEvent(aggId) {
    constructor(command: EditInternshipPostingCommand) : this(
        command.id.id,
        command.title,
        command.description,
        command.techStack
    )
    override fun getEventType() = "InternshipPostingEdited"
}

data class InternshipPostingPublishedEvent(
    val aggId: String,
    val publishedBy: String
) : InternshipPostingEvent(aggId) {
    constructor(command: PublishInternshipPostingCommand) : this(
        command.id.id,
        command.publishedBy
    )
    override fun getEventType() = "InternshipPostingPublished"
}

data class InternshipPostingDeletedEvent(
    val aggId: String
) : InternshipPostingEvent(aggId) {
    constructor(command: DeleteInternshipPostingCommand) : this(
        command.id.id
    )
    override fun getEventType() = "InternshipPostingDeleted"
}