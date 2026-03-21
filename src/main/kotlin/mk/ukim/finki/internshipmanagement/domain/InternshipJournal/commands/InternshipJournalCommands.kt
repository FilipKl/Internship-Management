package mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.CompanyName
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.InternshipJournal
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.InternshipJournalId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.JournalEntryId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.ProfessorId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateInternshipJournalCommand(

    val id: InternshipJournalId= InternshipJournalId.generate(),
    val companyName: CompanyName,
    val studentId: StudentId,
    val professorId: ProfessorId
)

data class UpdateJournalStatusCommand(

    @TargetAggregateIdentifier
    val id: InternshipJournalId,

    val isOngoing: Boolean
)

data class AddJournalEntryCommand(

    @TargetAggregateIdentifier
    val journalId: InternshipJournalId,
    val entryId: JournalEntryId
)

data class CompleteInternshipJournalCommand(

    @TargetAggregateIdentifier
    val id: InternshipJournalId
)