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
import mk.ukim.finki.internshipmanagement.domain.common.DomainEvent
import java.time.LocalDateTime
import java.util.UUID

abstract class InternshipJournalEvent(
    aggregateId: String,
    eventId: String = UUID.randomUUID().toString(),
    occurredAt: LocalDateTime = LocalDateTime.now()
) : DomainEvent(aggregateId, eventId, occurredAt)



data class InternshipJournalCreatedEvent(
    val aggId: String,
    val id: InternshipJournalId,
    val companyName: CompanyName,
    val studentId: StudentId,
    val professorId: ProfessorId,
): InternshipJournalEvent(aggId) {
    constructor(command: CreateInternshipJournalCommand) : this(
        aggId = command.id.value,
        id = command.id,
        companyName = command.companyName,
        studentId = command.studentId,
        professorId = command.professorId
    )

    override fun getEventType() = "InternshipJournalCreatedEvent"
}


// doesn't really make sense to add a third one, I think it would just create issues and it's just the same thing I think !
data class JournalStatusUpdatedEvent(
    val aggId: String,
    val isOngoing: Boolean
) : InternshipJournalEvent(aggId) {

    constructor(command: UpdateJournalStatusCommand) : this(
        aggId = command.id.value,
        isOngoing = command.isOngoing
    )

    override fun getEventType() = "JournalStatusUpdatedEvent"
}

data class JournalEntryAddedEvent(
    val aggId: String,
    val journalId: InternshipJournalId,
    val entryId: JournalEntryId,
) : InternshipJournalEvent(aggId) {

    constructor(command: AddJournalEntryCommand) : this(
        aggId=command.journalId.toString(),
        journalId=command.journalId,
        entryId = command.entryId
    )

    override fun getEventType()="JournalEntryAddedEvent"

}

data class InternshipJournalCompletedEvent(
    val aggId: String
) : InternshipJournalEvent(aggId) {

    constructor(command: CompleteInternshipJournalCommand) : this(
        aggId = command.id.value
    )

    override fun getEventType()= "InternshipJournalCompletedEvent"
    }



