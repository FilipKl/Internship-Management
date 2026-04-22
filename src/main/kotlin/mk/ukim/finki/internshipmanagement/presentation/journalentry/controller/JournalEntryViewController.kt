package mk.ukim.finki.internshipmanagement.presentation.journalentry.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mk.ukim.finki.internshipmanagement.application.journalentry.query.JournalEntryViewReadService
import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntry
import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntryId
import mk.ukim.finki.internshipmanagement.application.journalentry.query.JournalEntryView
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST Controller for JournalEntry read operations.
 * Provides HTTP endpoints for querying the JournalEntry read model.
 */
@RestController
@RequestMapping("/api/v1/journal-entries")
@Tag(
    name = "Journal Entry Query API",
    description = "API for querying journal entry data. Browse entries by ID, status, journal, and reviewer."
)
class JournalEntryViewController(
    private val readService: JournalEntryViewReadService
) {
    
    @Operation(
        summary = "Get all journal entries",
        description = "Retrieves all journal entries in the system."
    )
    @GetMapping("/all")
    fun findAll(): ResponseEntity<List<JournalEntryView>> =
        ResponseEntity.ok(readService.findAll())
    
    @Operation(
        summary = "Get journal entry by ID",
        description = "Retrieves a specific journal entry by its unique identifier."
    )
    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<JournalEntryView> =
        ResponseEntity.ok(readService.findById(JournalEntryId.from(id)))
    
    @Operation(
        summary = "Find entries by status",
        description = "Retrieves all journal entries filtered by their approval status (DRAFT, VALIDATED, REJECTED)."
    )
    @GetMapping("/by-status/{status}")
    fun findByStatus(@PathVariable status: String): ResponseEntity<List<JournalEntryView>> =
        ResponseEntity.ok(readService.findByStatus(JournalEntry.EntryStatus.valueOf(status.uppercase())))
    
    @Operation(
        summary = "Find entries by journal ID",
        description = "Retrieves all journal entries belonging to a specific internship journal."
    )
    @GetMapping("/journal/{journalId}")
    fun findByJournalId(@PathVariable journalId: String): ResponseEntity<List<JournalEntryView>> =
        ResponseEntity.ok(readService.findByJournalId(journalId))
    
    @Operation(
        summary = "Find draft entries by journal",
        description = "Retrieves all draft (unvalidated) entries in a specific internship journal."
    )
    @GetMapping("/journal/{journalId}/draft")
    fun findDraftEntriesByJournal(@PathVariable journalId: String): ResponseEntity<List<JournalEntryView>> =
        ResponseEntity.ok(readService.findDraftEntriesByJournal(journalId))
    
    @Operation(
        summary = "Find rejected entries by journal",
        description = "Retrieves all rejected entries in a specific internship journal."
    )
    @GetMapping("/journal/{journalId}/rejected")
    fun findRejectedEntriesByJournal(@PathVariable journalId: String): ResponseEntity<List<JournalEntryView>> =
        ResponseEntity.ok(readService.findRejectedEntriesByJournal(journalId))
    
    @Operation(
        summary = "Find entries reviewed by a specific reviewer",
        description = "Retrieves all journal entries that were validated or rejected by a specific reviewer."
    )
    @GetMapping("/reviewer/{reviewerId}")
    fun findByReviewedBy(@PathVariable reviewerId: String): ResponseEntity<List<JournalEntryView>> =
        ResponseEntity.ok(readService.findByReviewedBy(reviewerId))
    
    @Operation(
        summary = "Count entries by journal and status",
        description = "Returns the count of journal entries in a specific journal with a particular status."
    )
    @GetMapping("/journal/{journalId}/count/{status}")
    fun countByJournalIdAndStatus(
        @PathVariable journalId: String,
        @PathVariable status: String
    ): ResponseEntity<Long> =
        ResponseEntity.ok(readService.countByJournalIdAndStatus(
            journalId,
            JournalEntry.EntryStatus.valueOf(status.uppercase())
        ))
}

