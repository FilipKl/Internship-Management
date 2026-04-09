package mk.ukim.finki.internshipmanagement.domain.journalentry.events

import mk.ukim.finki.internshipmanagement.domain.common.AbstractEvent
import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntryId
import mk.ukim.finki.internshipmanagement.domain.journalentry.commands.*
import java.time.LocalDateTime
import java.util.*

/**
 * Base class for all JournalEntry domain events.
 * Extends AbstractEvent to support Kafka publishing.
 */
abstract class JournalEntryEvent(
    open val journalEntryId: JournalEntryId
) : AbstractEvent(journalEntryId)

// External event for JournalEntry creation
data class JournalEntryCreatedExternalEvent(
    val id: JournalEntryId,
    val journalId: String,
    val title: String,
    val content: String
)

// Creation Event
data class JournalEntryCreatedEvent(
    override val journalEntryId: JournalEntryId,
    val journalId: String,
    val title: String,
    val content: String
) : JournalEntryEvent(journalEntryId) {

    constructor(command: CreateJournalEntryCommand) : this(
        command.id,
        command.journalId,
        command.titleText,
        command.contentText
    )
    
    override fun toExternalEvent(): JournalEntryCreatedExternalEvent {
        return JournalEntryCreatedExternalEvent(
            id = journalEntryId,
            journalId = journalId,
            title = title,
            content = content
        )
    }
}

// Update Events (internal - do not publish)
data class EntryTitleUpdatedEvent(
    override val journalEntryId: JournalEntryId,
    val newTitle: String
) : JournalEntryEvent(journalEntryId) {

    constructor(command: UpdateEntryTitleCommand) : this(
        command.entryId,
        command.newTitleText
    )
}

data class EntryContentUpdatedEvent(
    override val journalEntryId: JournalEntryId,
    val newContent: String
) : JournalEntryEvent(journalEntryId) {

    constructor(command: UpdateEntryContentCommand) : this(
        command.entryId,
        command.newContentText
    )
}

// External event for validation
data class JournalEntryValidatedExternalEvent(
    val id: JournalEntryId,
    val validatedBy: String
)

// External event for rejection
data class JournalEntryRejectedExternalEvent(
    val id: JournalEntryId,
    val rejectedBy: String,
    val rejectionReason: String
)

// Status Events
data class JournalEntryValidatedEvent(
    override val journalEntryId: JournalEntryId,
    val validatedBy: String
) : JournalEntryEvent(journalEntryId) {

    constructor(command: ValidateJournalEntryCommand) : this(
        command.entryId,
        command.validatedBy
    )
    
    override fun toExternalEvent(): JournalEntryValidatedExternalEvent {
        return JournalEntryValidatedExternalEvent(
            id = journalEntryId,
            validatedBy = validatedBy
        )
    }
}

data class JournalEntryRejectedEvent(
    override val journalEntryId: JournalEntryId,
    val rejectedBy: String,
    val rejectionReason: String
) : JournalEntryEvent(journalEntryId) {

    constructor(command: RejectJournalEntryCommand) : this(
        command.entryId,
        command.rejectedBy,
        command.rejectionReason
    )
    
    override fun toExternalEvent(): JournalEntryRejectedExternalEvent {
        return JournalEntryRejectedExternalEvent(
            id = journalEntryId,
            rejectedBy = rejectedBy,
            rejectionReason = rejectionReason
        )
    }
}

// Legacy Events (internal - do not publish)
data class JournalEntryEditedEvent(
    override val journalEntryId: JournalEntryId,
    val journalId: String,
    val title: String,
    val content: String
) : JournalEntryEvent(journalEntryId)

data class JournalEntryAddedEvent(
    override val journalEntryId: JournalEntryId,
    val journalId: String,
    val title: String,
    val content: String
) : JournalEntryEvent(journalEntryId)

