package mk.ukim.finki.internshipmanagement.domain.InternshipJournal

import jakarta.persistence.*
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.AddJournalEntryCommand
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.CompleteInternshipJournalCommand
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.CreateInternshipJournalCommand
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.UpdateJournalStatusCommand
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.events.InternshipJournalCompletedEvent
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.events.InternshipJournalCreatedEvent
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.events.JournalEntryAddedEvent
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.events.JournalStatusUpdatedEvent
import mk.ukim.finki.internshipmanagement.domain.common.AggregateRoot
import mk.ukim.finki.internshipmanagement.domain.common.LabeledEntity
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate
import java.time.LocalDateTime

/**
 * InternshipJournal is the aggregate root representing an internship journal.
 * A student uses this to track their daily/weekly progress during internship.
 *
 * Immutable properties: id, dateCreated, companyName, studentId, professorId
 * Mutable properties: entries, isOngoing
 */

@Aggregate
@Entity
@Suppress("JpaDataSourceORMInspection")
@Table(name = "internship_journal")
class InternshipJournal : LabeledEntity,AggregateRoot {

    @AggregateIdentifier
    @EmbeddedId
    lateinit var internshipJournalId: InternshipJournalId

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "company_name"))
    lateinit var companyName: CompanyName
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "student_id"))
    lateinit var studentId: StudentId
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "professor_id"))
    lateinit var professorId: ProfessorId
    var isOngoing: Boolean = true
    @ElementCollection
    @CollectionTable(
        name = "internship_journal_entries",
        joinColumns = [JoinColumn(name = "journal_id")]
    )
    @AttributeOverride(name = "value", column = Column(name = "entry_id"))
    val entries: MutableList<JournalEntryId> = mutableListOf()
    lateinit var dateCreated: LocalDateTime

    @Enumerated(EnumType.STRING)
    lateinit var status: InternshipJournalStatus

    // ----------------------
    // Command handler: creation
    // ----------------------

    // ----------------------
    // CommandHandler constructor for creation
    // ----------------------

    constructor()

    constructor(command: CreateInternshipJournalCommand) : super() {
        apply(InternshipJournalCreatedEvent(command))
    }

    // ----------------------
    // Event sourcing handler
    // ----------------------
    @EventSourcingHandler
    fun on(event: InternshipJournalCreatedEvent) {
        internshipJournalId = event.id
        companyName = event.companyName
        studentId = event.studentId
        professorId = event.professorId
        isOngoing = true
        entries.clear()
        dateCreated = LocalDateTime.now()
        status = InternshipJournalStatus.ACTIVE
    }

    @CommandHandler
    fun handle(command: UpdateJournalStatusCommand) {
        apply(JournalStatusUpdatedEvent(command))
    }
    @CommandHandler
    fun handle(command: CompleteInternshipJournalCommand) {

        check(status != InternshipJournalStatus.CANCELLED) {
            "Cannot complete a cancelled internship journal"
        }

        check(status != InternshipJournalStatus.COMPLETED) {
            "Internship journal is already completed"
        }

        apply(InternshipJournalCompletedEvent(command))
    }

    @EventSourcingHandler
    fun on(@Suppress("UNUSED_PARAMETER") event: InternshipJournalCompletedEvent) {
        status = InternshipJournalStatus.COMPLETED
    }

    @EventSourcingHandler
    fun on(event: JournalStatusUpdatedEvent) {
        isOngoing = event.isOngoing
    }

    @CommandHandler
    fun handle(command: AddJournalEntryCommand) {
        apply(JournalEntryAddedEvent(command))
    }

    @EventSourcingHandler
    fun on(event: JournalEntryAddedEvent) {
        entries.add(event.entryId)
    }

    // ----------------------
    // LabeledEntity implementation
    // ----------------------
    override fun getId(): InternshipJournalId = internshipJournalId
    override fun getLabel(): String = "Internship journal of student ${studentId} at ${companyName}"
}


enum class InternshipJournalStatus{
    ACTIVE,
    COMPLETED,
    CANCELLED
}