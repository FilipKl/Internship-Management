package mk.ukim.finki.internshipmanagement.domain.journalentry.commands

import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntryId

data class CreateJournalEntryCommand(
    val id: JournalEntryId,
    val journalId: String,
    val titleText: String,
    val contentText: String
)

data class UpdateEntryTitleCommand(
    val entryId: JournalEntryId,
    val newTitleText: String
)

data class UpdateEntryContentCommand(
    val entryId: JournalEntryId,
    val newContentText: String
)

data class ValidateJournalEntryCommand(
    val entryId: JournalEntryId,
    val validatedBy: String
)

data class RejectJournalEntryCommand(
    val entryId: JournalEntryId,
    val rejectedBy: String,
    val rejectionReason: String
)

