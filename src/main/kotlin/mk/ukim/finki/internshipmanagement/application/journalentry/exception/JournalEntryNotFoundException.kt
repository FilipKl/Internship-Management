package mk.ukim.finki.internshipmanagement.application.journalentry.exception

import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntryId

class JournalEntryNotFoundException(
    val entryId: String,
    message: String = "Journal entry not found: $entryId"
) : RuntimeException(message) {

    constructor(id: JournalEntryId) : this(id.id, "Journal entry not found: ${id.id}")
}