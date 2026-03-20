package mk.ukim.finki.internshipmanagement.application.journalentry.query

import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntry
import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntryId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * Repository for JournalEntryView read model.
 * Provides read-only access to journal entries with custom finders.
 */
@Repository
interface JournalEntryViewRepository : JpaRepository<JournalEntryView, JournalEntryId> {

    fun findByStatus(status: JournalEntry.EntryStatus): List<JournalEntryView>

    fun findByJournalId(journalId: String): List<JournalEntryView>

    fun findByJournalIdAndStatus(journalId: String, status: JournalEntry.EntryStatus): List<JournalEntryView>

    @Query("""
        SELECT je FROM JournalEntryView je 
        WHERE je.createdAt >= :startDate AND je.createdAt <= :endDate 
        ORDER BY je.createdAt DESC
    """)
    fun findByCreatedAtRange(
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<JournalEntryView>

    fun findByReviewedBy(reviewerId: String): List<JournalEntryView>

    fun countByJournalIdAndStatus(journalId: String, status: JournalEntry.EntryStatus): Long
}