package mk.ukim.finki.internshipmanagement.application.InternshipJournal.query

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.InternshipJournalId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.InternshipJournalStatus
import org.springframework.stereotype.Service

interface InternshipJournalQueryService {
    fun findAll(): List<InternshipJournalView>
    fun findById(id: InternshipJournalId): InternshipJournalView
    fun findByStatus(status: InternshipJournalStatus): List<InternshipJournalView>
}

@Service
class InternshipJournalQueryServiceImpl(
    private val repository: InternshipJournalViewJpaRepository
) : InternshipJournalQueryService {

    override fun findAll(): List<InternshipJournalView> =
        repository.findAll()

    override fun findById(id: InternshipJournalId): InternshipJournalView =
        repository.findById(id)
            .orElseThrow { NoSuchElementException("InternshipJournal with id $id not found") }
    override fun findByStatus(status: InternshipJournalStatus): List<InternshipJournalView> =
        repository.findByStatus(status)
}