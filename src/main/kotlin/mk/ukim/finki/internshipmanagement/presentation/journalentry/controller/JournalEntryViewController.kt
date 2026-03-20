package mk.ukim.finki.internshipmanagement.presentation.journalentry.controller

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
class JournalEntryViewController(
    private val readService: JournalEntryViewReadService
) {
    
    @GetMapping("/all")
    fun findAll(): ResponseEntity<List<JournalEntryView>> =
        ResponseEntity.ok(readService.findAll())
    
    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<JournalEntryView> =
        ResponseEntity.ok(readService.findById(JournalEntryId.from(id)))
    
    @GetMapping("/by-status/{status}")
    fun findByStatus(@PathVariable status: String): ResponseEntity<List<JournalEntryView>> =
        ResponseEntity.ok(readService.findByStatus(JournalEntry.EntryStatus.valueOf(status.uppercase())))
    
    @GetMapping("/journal/{journalId}")
    fun findByJournalId(@PathVariable journalId: String): ResponseEntity<List<JournalEntryView>> =
        ResponseEntity.ok(readService.findByJournalId(journalId))
    
    @GetMapping("/journal/{journalId}/draft")
    fun findDraftEntriesByJournal(@PathVariable journalId: String): ResponseEntity<List<JournalEntryView>> =
        ResponseEntity.ok(readService.findDraftEntriesByJournal(journalId))
    
    @GetMapping("/journal/{journalId}/rejected")
    fun findRejectedEntriesByJournal(@PathVariable journalId: String): ResponseEntity<List<JournalEntryView>> =
        ResponseEntity.ok(readService.findRejectedEntriesByJournal(journalId))
    
    @GetMapping("/reviewer/{reviewerId}")
    fun findByReviewedBy(@PathVariable reviewerId: String): ResponseEntity<List<JournalEntryView>> =
        ResponseEntity.ok(readService.findByReviewedBy(reviewerId))
    
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

