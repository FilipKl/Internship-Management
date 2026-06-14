package mk.ukim.finki.internshipmanagement.application.journalentry.query

import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntry
import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntryId
import mk.ukim.finki.internshipmanagement.application.journalentry.exception.JournalEntryNotFoundException
import mk.ukim.finki.internshipmanagement.application.journalentry.query.JournalEntryView
import mk.ukim.finki.internshipmanagement.application.journalentry.query.JournalEntryViewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

interface JournalEntryViewReadService {
    fun findAll(): List<JournalEntryView>
    fun findById(id: JournalEntryId): JournalEntryView
    fun findByStatus(status: JournalEntry.EntryStatus): List<JournalEntryView>
    fun findByJournalId(journalId: String): List<JournalEntryView>
    fun findByJournalIdAndStatus(journalId: String, status: JournalEntry.EntryStatus): List<JournalEntryView>
    fun findByCreatedAtRange(startDate: LocalDateTime, endDate: LocalDateTime): List<JournalEntryView>
    fun findByReviewedBy(reviewerId: String): List<JournalEntryView>
    fun findDraftEntriesByJournal(journalId: String): List<JournalEntryView>
    fun findRejectedEntriesByJournal(journalId: String): List<JournalEntryView>
    fun countByJournalIdAndStatus(journalId: String, status: JournalEntry.EntryStatus): Long
}

@Service
@Transactional(readOnly = true)
class JournalEntryViewReadServiceImpl(
    private val viewRepository: JournalEntryViewRepository
) : JournalEntryViewReadService {
    
    override fun findAll() = viewRepository.findAll()
    
    override fun findById(id: JournalEntryId) =
        viewRepository.findById(id).orElseThrow { JournalEntryNotFoundException(id) }
    
    override fun findByStatus(status: JournalEntry.EntryStatus) = viewRepository.findByStatus(status)
    
    override fun findByJournalId(journalId: String) = viewRepository.findByJournalId(journalId)
    
    override fun findByJournalIdAndStatus(journalId: String, status: JournalEntry.EntryStatus) =
        viewRepository.findByJournalIdAndStatus(journalId, status)
    
    override fun findByCreatedAtRange(startDate: LocalDateTime, endDate: LocalDateTime) =
        viewRepository.findByCreatedAtRange(startDate, endDate)
    
    override fun findByReviewedBy(reviewerId: String) = viewRepository.findByReviewedBy(reviewerId)
    
    override fun findDraftEntriesByJournal(journalId: String) =
        viewRepository.findByJournalIdAndStatus(journalId, JournalEntry.EntryStatus.DRAFT)
    
    override fun findRejectedEntriesByJournal(journalId: String) =
        viewRepository.findByJournalIdAndStatus(journalId, JournalEntry.EntryStatus.REJECTED)
    
    override fun countByJournalIdAndStatus(journalId: String, status: JournalEntry.EntryStatus) =
        viewRepository.countByJournalIdAndStatus(journalId, status)
}

