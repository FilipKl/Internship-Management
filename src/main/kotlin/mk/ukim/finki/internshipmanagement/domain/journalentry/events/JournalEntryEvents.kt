package mk.ukim.finki.internshipmanagement.domain.journalentry.events

import mk.ukim.finki.internshipmanagement.domain.common.DomainEvent
import mk.ukim.finki.internshipmanagement.domain.journalentry.commands.*
import java.time.LocalDateTime
import java.util.*

/**
 * Base class for all JournalEntry domain events.
 */
abstract class JournalEntryEvent(
    aggregateId: String,
    eventId: String = UUID.randomUUID().toString(),
    occurredAt: LocalDateTime = LocalDateTime.now()
) : DomainEvent(aggregateId, eventId, occurredAt)

// Creation Event
data class JournalEntryCreatedEvent(
    val aggId: String,
    val journalId: String,
    val title: String,
    val content: String
) : JournalEntryEvent(aggId) {
    
    constructor(command: CreateJournalEntryCommand) : this(
        command.id.id,
        command.journalId,
        command.titleText,
        command.contentText
    )
    
    override fun getEventType() = "JournalEntryCreated"
}

// Update Events
data class EntryTitleUpdatedEvent(
    val aggId: String,
    val newTitle: String
) : JournalEntryEvent(aggId) {
    
    constructor(command: UpdateEntryTitleCommand) : this(
        command.entryId.id,
        command.newTitleText
    )
    
    override fun getEventType() = "EntryTitleUpdated"
}

data class EntryContentUpdatedEvent(
    val aggId: String,
    val newContent: String
) : JournalEntryEvent(aggId) {
    
    constructor(command: UpdateEntryContentCommand) : this(
        command.entryId.id,
        command.newContentText
    )
    
    override fun getEventType() = "EntryContentUpdated"
}

// Status Events
data class JournalEntryValidatedEvent(
    val aggId: String,
    val validatedBy: String
) : JournalEntryEvent(aggId) {
    
    constructor(command: ValidateJournalEntryCommand) : this(
        command.entryId.id,
        command.validatedBy
    )
    
    override fun getEventType() = "JournalEntryValidated"
}

data class JournalEntryRejectedEvent(
    val aggId: String,
    val rejectedBy: String,
    val rejectionReason: String
) : JournalEntryEvent(aggId) {
    
    constructor(command: RejectJournalEntryCommand) : this(
        command.entryId.id,
        command.rejectedBy,
        command.rejectionReason
    )
    
    override fun getEventType() = "JournalEntryRejected"
}

// Legacy Events
data class JournalEntryEditedEvent(
    val aggId: String,
    val journalId: String,
    val title: String,
    val content: String
) : JournalEntryEvent(aggId) {
    override fun getEventType() = "JournalEntryEdited"
}

data class JournalEntryAddedEvent(
    val aggId: String,
    val journalId: String,
    val title: String,
    val content: String
) : JournalEntryEvent(aggId) {
    override fun getEventType() = "JournalEntryAdded"
}

