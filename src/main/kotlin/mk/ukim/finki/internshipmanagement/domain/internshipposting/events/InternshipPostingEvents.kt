package mk.ukim.finki.internshipmanagement.domain.internshipposting.events

import mk.ukim.finki.internshipmanagement.domain.common.AbstractEvent
import mk.ukim.finki.internshipmanagement.domain.internshipposting.InternshipPostingId
import mk.ukim.finki.internshipmanagement.domain.internshipposting.commands.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


abstract class InternshipPostingEvent(
    open val internshipPostingId: InternshipPostingId
) : AbstractEvent(internshipPostingId)


data class InternshipPostingCreatedExternalEvent(
    val id: InternshipPostingId,
    val title: String,
    val company: String,
    val description: String,
    val techStack: String,
    val location: String
)

data class InternshipPostingCreatedEvent(
    override val internshipPostingId: InternshipPostingId,
    val title: String,
    val company: String,
    val description: String,
    val postedDate: LocalDate,
    val validUntil: LocalDate,
    val location: String,
    val techStack: String,
    val contactEmail: String
) : InternshipPostingEvent(internshipPostingId) {
    constructor(command: CreateInternshipPostingCommand) : this(
        command.id,
        command.title,
        command.company,
        command.description,
        command.postedDate,
        command.validUntil,
        command.location,
        command.techStack,
        command.contactEmail
    )

    override fun toExternalEvent(): InternshipPostingCreatedExternalEvent {
        return InternshipPostingCreatedExternalEvent(
            id = internshipPostingId,
            title = title,
            company = company,
            description = description,
            techStack = techStack,
            location = location
        )
    }
}

// Internal events - do not publish
data class InternshipPostingUpdatedEvent(
    override val internshipPostingId: InternshipPostingId,
    val title: String,
    val description: String,
    val techStack: String,
    val company: String,
    val location: String
) : InternshipPostingEvent(internshipPostingId) {
    constructor(command: UpdateInternshipPostingCommand) : this(
        command.id,
        command.title,
        command.description,
        command.techStack,
        command.company,
        command.location
    )
}

data class InternshipPostingEditedEvent(
    override val internshipPostingId: InternshipPostingId,
    val title: String,
    val description: String,
    val techStack: String
) : InternshipPostingEvent(internshipPostingId) {
    constructor(command: EditInternshipPostingCommand) : this(
        command.id,
        command.title,
        command.description,
        command.techStack
    )
}


data class InternshipPostingPublishedExternalEvent(
    val id: InternshipPostingId,
    val publishedBy: String
)

data class InternshipPostingPublishedEvent(
    override val internshipPostingId: InternshipPostingId,
    val publishedBy: String
) : InternshipPostingEvent(internshipPostingId) {
    constructor(command: PublishInternshipPostingCommand) : this(
        command.id,
        command.publishedBy
    )

    override fun toExternalEvent(): InternshipPostingPublishedExternalEvent {
        return InternshipPostingPublishedExternalEvent(
            id = internshipPostingId,
            publishedBy = publishedBy
        )
    }
}


data class InternshipPostingDeletedEvent(
    override val internshipPostingId: InternshipPostingId
) : InternshipPostingEvent(internshipPostingId) {
    constructor(command: DeleteInternshipPostingCommand) : this(
        command.id
    )
}