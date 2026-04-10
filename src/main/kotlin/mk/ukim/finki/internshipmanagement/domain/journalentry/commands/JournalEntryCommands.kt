package mk.ukim.finki.internshipmanagement.domain.journalentry.commands

import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntryId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateJournalEntryCommand(
    @TargetAggregateIdentifier
    val id: JournalEntryId,
    val journalId: String,
    val titleText: String,
    val contentText: String
)

data class UpdateEntryTitleCommand(
    @TargetAggregateIdentifier
    val entryId: JournalEntryId,
    val newTitleText: String
)

data class UpdateEntryContentCommand(
    @TargetAggregateIdentifier
    val entryId: JournalEntryId,
    val newContentText: String
)

data class ValidateJournalEntryCommand(
    @TargetAggregateIdentifier
    val entryId: JournalEntryId,
    val validatedBy: String
)

data class RejectJournalEntryCommand(
    @TargetAggregateIdentifier
    val entryId: JournalEntryId,
    val rejectedBy: String,
    val rejectionReason: String
)

