package mk.ukim.finki.internshipmanagement.presentation.InternshipJournal.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mk.ukim.finki.internshipmanagement.application.InternshipJournal.query.*
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/internship-journals")
@Tag(
    name = "Internship Journal Query API",
    description = "API for querying internship journal data. Browse journals by ID and status."
)
class InternshipJournalController(
    private val readService: InternshipJournalQueryService
) {

    @Operation(
        summary = "Get all internship journals",
        description = "Retrieves all internship journals in the system."
    )
    @GetMapping
    fun getAll(): List<InternshipJournalView> = readService.findAll()

    @Operation(
        summary = "Get internship journal by ID",
        description = "Retrieves a specific internship journal by its unique identifier."
    )
    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): InternshipJournalView {
        val journalId = InternshipJournalId(id = id)
        return readService.findById(journalId)
    }

    @Operation(
        summary = "Get journals by status",
        description = "Retrieves all internship journals filtered by their status (CREATED, ONGOING, COMPLETED)."
    )
    @GetMapping("/by-status/{status}")
    fun getByStatus(@PathVariable status: String): List<InternshipJournalView> {
        val enumStatus = InternshipJournalStatus.valueOf(status.uppercase())
        return readService.findByStatus(enumStatus)
    }
}

