package mk.ukim.finki.internshipmanagement.domain.journalentry

import jakarta.persistence.*
import mk.ukim.finki.internshipmanagement.domain.common.AggregateRoot
import mk.ukim.finki.internshipmanagement.domain.journalentry.commands.*
import mk.ukim.finki.internshipmanagement.domain.journalentry.events.*
import java.time.LocalDateTime

/**
 * JournalEntry Aggregate Root
 * Manages journal entry lifecycle: Create, Edit, Validate, Reject
 * 
 * Uses value objects for type-safe domain modeling:
 * - JournalEntryId: Strongly-typed identifier with "JournalEntry:" prefix
 * - EntryTitle: Validated title (1-255 chars)
 * - EntryContent: Validated content (10-50000 chars)
 */
@Entity
@Table(name = "journal_entries")
class JournalEntry : AggregateRoot {
    
    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    lateinit var journalEntryId: JournalEntryId
    
    @Column(nullable = false)
    lateinit var journalId: String
    
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "title"))
    lateinit var title: EntryTitle
    
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "content"))
    lateinit var content: EntryContent
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    lateinit var status: EntryStatus
    
    @Column(nullable = false)
    lateinit var createdAt: LocalDateTime
    
    var editedAt: LocalDateTime? = null
    var reviewedAt: LocalDateTime? = null
    var reviewedBy: String? = null
    var reviewReason: String? = null

    constructor()
    
    /**
     * Command Handler: Create a new JournalEntry.
     */
    constructor(command: CreateJournalEntryCommand) {
        journalEntryId = JournalEntryId.generate()
        status = EntryStatus.DRAFT
        recordEvent(JournalEntryCreatedEvent(command))
    }
    
    // Event Handlers
    fun on(event: JournalEntryCreatedEvent) {
        journalEntryId = JournalEntryId.from(event.aggregateId)
        journalId = event.journalId
        title = EntryTitle(event.title)
        content = EntryContent(event.content)
        status = EntryStatus.DRAFT
        createdAt = LocalDateTime.now()
    }
    
    // Command Handlers - Update Title
    fun handle(command: UpdateEntryTitleCommand) {
        check(status == EntryStatus.DRAFT) { "Only draft entries can be edited" }
        recordEvent(EntryTitleUpdatedEvent(command))
    }
    
    fun on(event: EntryTitleUpdatedEvent) {
        title = EntryTitle(event.newTitle)
        editedAt = LocalDateTime.now()
    }
    
    // Command Handlers - Update Content
    fun handle(command: UpdateEntryContentCommand) {
        check(status == EntryStatus.DRAFT) { "Only draft entries can be edited" }
        recordEvent(EntryContentUpdatedEvent(command))
    }
    
    fun on(event: EntryContentUpdatedEvent) {
        content = EntryContent(event.newContent)
        editedAt = LocalDateTime.now()
    }
    
    // Command Handlers - Validate
    fun handle(command: ValidateJournalEntryCommand) {
        check(status == EntryStatus.DRAFT) { "Only draft entries can be validated" }
        recordEvent(JournalEntryValidatedEvent(command))
    }
    
    fun on(event: JournalEntryValidatedEvent) {
        status = EntryStatus.VALIDATED
        reviewedAt = LocalDateTime.now()
        reviewedBy = event.validatedBy
    }
    
    // Command Handlers - Reject
    fun handle(command: RejectJournalEntryCommand) {
        check(status != EntryStatus.REJECTED) { "Entry is already rejected" }
        check(status == EntryStatus.DRAFT) { "Only draft entries can be rejected" }
        recordEvent(JournalEntryRejectedEvent(command))
    }
    
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
        recordEvent(JournalEntryEditedEvent(journalEntryId.id, journalId, titleText, contentText))
        on(JournalEntryEditedEvent(journalEntryId.id, journalId, titleText, contentText))
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

