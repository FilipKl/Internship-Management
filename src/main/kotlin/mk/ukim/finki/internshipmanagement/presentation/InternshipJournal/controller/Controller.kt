package mk.ukim.finki.internshipmanagement.presentation.InternshipJournal.controller


import mk.ukim.finki.internshipmanagement.application.InternshipJournal.query.*
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/internship-journal")
class InternshipJournalController(
    private val readService: InternshipJournalQueryService
) {

    @GetMapping("/all")
    fun getAll(): List<InternshipJournalView> = readService.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): InternshipJournalView {
        val journalId = InternshipJournalId(id = id)
        return readService.findById(journalId)
    }

    @GetMapping("/by-status/{status}")
    fun getByStatus(@PathVariable status: String): List<InternshipJournalView> {
        val enumStatus = InternshipJournalStatus.valueOf(status.uppercase())
        return readService.findByStatus(enumStatus)
    }
}