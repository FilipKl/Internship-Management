package mk.ukim.finki.internshipmanagement.application.journalentry.exception

import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntryId
import java.util.*

class JournalEntryNotFoundException(
    val entryId: UUID,
    message: String = "Journal entry not found: $entryId"
) : RuntimeException(message) {

    constructor(id: JournalEntryId) : this(id.id, "Journal entry not found: ${id.id}")
}