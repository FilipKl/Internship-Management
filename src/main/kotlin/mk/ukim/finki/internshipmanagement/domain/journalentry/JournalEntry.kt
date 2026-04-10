package mk.ukim.finki.internshipmanagement.domain.journalentry

import mk.ukim.finki.internshipmanagement.domain.journalentry.commands.*
import mk.ukim.finki.internshipmanagement.domain.journalentry.events.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.time.LocalDateTime

/**
 * JournalEntry Aggregate Root
 * Manages journal entry lifecycle: Create, Edit, Validate, Reject
 * 
 * Uses Axon event sourcing - state is reconstructed from events, not persisted directly.
 *
 * Uses value objects for type-safe domain modeling:
 * - JournalEntryId: Strongly-typed identifier with "JournalEntry:" prefix
 * - EntryTitle: Validated title (1-255 chars)
 * - EntryContent: Validated content (10-50000 chars)
 */
@Aggregate
class JournalEntry {

    @AggregateIdentifier
    lateinit var journalEntryId: JournalEntryId
    
    lateinit var journalId: String
    
    lateinit var title: EntryTitle
    
    lateinit var content: EntryContent
    
    lateinit var status: EntryStatus
    
    lateinit var createdAt: LocalDateTime
    
    var editedAt: LocalDateTime? = null
    var reviewedAt: LocalDateTime? = null
    var reviewedBy: String? = null
    var reviewReason: String? = null

    constructor()
    
    /**
     * Command Handler: Create a new JournalEntry.
     */
    @CommandHandler
    constructor(command: CreateJournalEntryCommand) {
        AggregateLifecycle.apply(JournalEntryCreatedEvent(
            journalEntryId = command.id,
            journalId = command.journalId,
            title = command.titleText,
            content = command.contentText
        ))
    }
    
    // Event Handlers
    @EventSourcingHandler
    fun on(event: JournalEntryCreatedEvent) {
        journalEntryId = event.journalEntryId
        journalId = event.journalId
        title = EntryTitle(event.title)
        content = EntryContent(event.content)
        status = EntryStatus.DRAFT
        createdAt = LocalDateTime.now()
    }
    
    // Command Handlers - Update Title
    @CommandHandler
    fun handle(command: UpdateEntryTitleCommand) {
        check(status == EntryStatus.DRAFT) { "Only draft entries can be edited" }
        AggregateLifecycle.apply(EntryTitleUpdatedEvent(
            journalEntryId = journalEntryId,
            newTitle = command.newTitleText
        ))
    }
    
    @EventSourcingHandler
    fun on(event: EntryTitleUpdatedEvent) {
        title = EntryTitle(event.newTitle)
        editedAt = LocalDateTime.now()
    }
    
    // Command Handlers - Update Content
    @CommandHandler
    fun handle(command: UpdateEntryContentCommand) {
        check(status == EntryStatus.DRAFT) { "Only draft entries can be edited" }
        AggregateLifecycle.apply(EntryContentUpdatedEvent(
            journalEntryId = journalEntryId,
            newContent = command.newContentText
        ))
    }
    
    @EventSourcingHandler
    fun on(event: EntryContentUpdatedEvent) {
        content = EntryContent(event.newContent)
        editedAt = LocalDateTime.now()
    }
    
    // Command Handlers - Validate
    @CommandHandler
    fun handle(command: ValidateJournalEntryCommand) {
        check(status == EntryStatus.DRAFT) { "Only draft entries can be validated" }
        AggregateLifecycle.apply(JournalEntryValidatedEvent(
            journalEntryId = journalEntryId,
            validatedBy = command.validatedBy
        ))
    }
    
    @EventSourcingHandler
    fun on(event: JournalEntryValidatedEvent) {
        status = EntryStatus.VALIDATED
        reviewedAt = LocalDateTime.now()
        reviewedBy = event.validatedBy
    }
    
    // Command Handlers - Reject
    @CommandHandler
    fun handle(command: RejectJournalEntryCommand) {
        check(status != EntryStatus.REJECTED) { "Entry is already rejected" }
        check(status == EntryStatus.DRAFT) { "Only draft entries can be rejected" }
        AggregateLifecycle.apply(JournalEntryRejectedEvent(
            journalEntryId = journalEntryId,
            rejectedBy = command.rejectedBy,
            rejectionReason = command.rejectionReason
        ))
    }
    
    @EventSourcingHandler
    fun on(event: JournalEntryRejectedEvent) {
        status = EntryStatus.REJECTED
        reviewedAt = LocalDateTime.now()
        reviewedBy = event.rejectedBy
        reviewReason = event.rejectionReason
    }
    
    // Legacy Methods for backward compatibility
    @Deprecated("Use command handlers instead")
    fun edit(titleText: String, contentText: String) {
        check(status == EntryStatus.DRAFT) { "Only draft entries can be edited" }
        AggregateLifecycle.apply(JournalEntryEditedEvent(journalEntryId, journalId, titleText, contentText))
    }
    
    fun on(event: JournalEntryEditedEvent) {
        title = EntryTitle(event.title)
        content = EntryContent(event.content)
        editedAt = LocalDateTime.now()
    }
    
    // Status enum
    enum class EntryStatus {
        DRAFT, VALIDATED, REJECTED
    }
}

