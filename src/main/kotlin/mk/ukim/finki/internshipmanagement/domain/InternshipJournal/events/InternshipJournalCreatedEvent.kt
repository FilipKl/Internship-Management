package mk.ukim.finki.internshipmanagement.domain.InternshipJournal.events

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.CompanyName
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.InternshipJournalId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.JournalEntryId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.ProfessorId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.AddJournalEntryCommand
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.CompleteInternshipJournalCommand
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.CreateInternshipJournalCommand
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.UpdateJournalStatusCommand
import mk.ukim.finki.internshipmanagement.domain.common.AbstractEvent
import java.time.LocalDateTime

/**
 * Intermediate event base class for InternshipJournal aggregate.
 * All InternshipJournal events extend this class, which extends AbstractEvent.
 * This gives all InternshipJournal events automatic Kafka support.
 */
abstract class InternshipJournalEvent(
    open val internshipJournalId: InternshipJournalId
) : AbstractEvent(internshipJournalId)

/**
 * External event: InternshipJournal created
 * Published to Kafka for other microservices
 */
data class InternshipJournalCreatedExternalEvent(
    val id: InternshipJournalId,
    val companyName: CompanyName,
    val studentId: StudentId,
    val professorId: ProfessorId
)

data class InternshipJournalCreatedEvent(
    override val internshipJournalId: InternshipJournalId,
    val companyName: CompanyName,
    val studentId: StudentId,
    val professorId: ProfessorId,
): InternshipJournalEvent(internshipJournalId) {
    constructor(command: CreateInternshipJournalCommand) : this(
        internshipJournalId = command.id,
        companyName = command.companyName,
        studentId = command.studentId,
        professorId = command.professorId
    )

    override fun toExternalEvent(): InternshipJournalCreatedExternalEvent {
        return InternshipJournalCreatedExternalEvent(
            id = internshipJournalId,
            companyName = companyName,
            studentId = studentId,
            professorId = professorId
        )
    }
}

/**
 * Internal event: Journal status updated
 * Does NOT publish to Kafka (returns null from toExternalEvent)
 */
data class JournalStatusUpdatedEvent(
    override val internshipJournalId: InternshipJournalId,
    val isOngoing: Boolean
) : InternshipJournalEvent(internshipJournalId) {

    constructor(command: UpdateJournalStatusCommand) : this(
        internshipJournalId = command.id,
        isOngoing = command.isOngoing
    )
}

/**
 * Internal event: Journal entry added
 * Does NOT publish to Kafka (returns null from toExternalEvent)
 */
data class JournalEntryAddedEvent(
    override val internshipJournalId: InternshipJournalId,
    val entryId: JournalEntryId,
) : InternshipJournalEvent(internshipJournalId) {

    constructor(command: AddJournalEntryCommand) : this(
        internshipJournalId = command.journalId,
        entryId = command.entryId
    )
}

/**
 * External event: InternshipJournal completed
 * Published to Kafka for other microservices
 */
data class InternshipJournalCompletedExternalEvent(
    val id: InternshipJournalId,
    val completedAt: LocalDateTime = LocalDateTime.now()
)

data class InternshipJournalCompletedEvent(
    override val internshipJournalId: InternshipJournalId,
    val completedAt: LocalDateTime = LocalDateTime.now()
) : InternshipJournalEvent(internshipJournalId) {

    constructor(command: CompleteInternshipJournalCommand) : this(
        internshipJournalId = command.id
    )

    override fun toExternalEvent(): InternshipJournalCompletedExternalEvent {
        return InternshipJournalCompletedExternalEvent(
            id = internshipJournalId,
            completedAt = completedAt
        )
    }
}



