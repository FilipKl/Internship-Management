package mk.ukim.finki.internshipmanagement.presentation.InternshipJournal.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.CompanyName
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.InternshipJournalId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.JournalEntryId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.ProfessorId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.AddJournalEntryCommand
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.CompleteInternshipJournalCommand
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.CreateInternshipJournalCommand
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.UpdateJournalStatusCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for InternshipJournal write-side operations.
 */
@RestController
@RequestMapping("/api/v1/internship-journals")
@Tag(
    name = "Internship Journal Commands",
    description = "API for creating and managing internship journal lifecycle. Create journals, update status, add entries, and complete internships."
)
class InternshipJournalCommandController(
    private val commandGateway: CommandGateway
) {

    private fun toClientId(id: InternshipJournalId): String =
        id.toString()

    private fun toAggregateId(id: String): InternshipJournalId =
        InternshipJournalId.from(id)

    @Operation(
        summary = "Create a new internship journal",
        description = "Creates a new internship journal for a student to document their internship experience with a company."
    )
    @PostMapping
    fun createInternshipJournal(
        @RequestBody request: CreateInternshipJournalRequest
    ): ResponseEntity<Map<String, String>> {
        val journalId = InternshipJournalId.generate()
        val command = CreateInternshipJournalCommand(
            id = journalId,
            companyName = CompanyName(request.companyName),
            studentId = StudentId(request.studentId),
            professorId = ProfessorId(request.professorId)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("internshipJournalId" to toClientId(journalId), "status" to "ACTIVE"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to create journal")))
        }
    }

    @Operation(
        summary = "Update journal status",
        description = "Updates the status of an internship journal (mark as ongoing or completed)."
    )
    @PutMapping("/{id}/status")
    fun updateJournalStatus(
        @PathVariable id: String,
        @RequestBody request: UpdateJournalStatusRequest
    ): ResponseEntity<Map<String, String>> {
        val command = UpdateJournalStatusCommand(
            id = toAggregateId(id),
            isOngoing = request.isOngoing
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Journal status updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to update journal status")))
        }
    }

    @Operation(
        summary = "Add a journal entry",
        description = "Adds a journal entry to an internship journal."
    )
    @PostMapping("/{journalId}/entries")
    fun addJournalEntry(
        @PathVariable journalId: String,
        @RequestBody request: AddJournalEntryRequest
    ): ResponseEntity<Map<String, String>> {
        val command = AddJournalEntryCommand(
            journalId = toAggregateId(journalId),
            entryId = JournalEntryId(request.entryId)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Journal entry added successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to add journal entry")))
        }
    }

    @Operation(
        summary = "Complete an internship journal",
        description = "Marks an internship journal as completed, finalizing the student's internship documentation."
    )
    @PostMapping("/{id}/complete")
    fun completeInternshipJournal(
        @PathVariable id: String
    ): ResponseEntity<Map<String, String>> {
        val command = CompleteInternshipJournalCommand(
            id = toAggregateId(id)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Internship journal completed successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to complete journal")))
        }
    }
}

// ====== Request DTOs ======

/**
 * Request DTO for creating a new InternshipJournal
 */
data class CreateInternshipJournalRequest(
    val companyName: String,
    val studentId: String,
    val professorId: String
)

/**
 * Request DTO for updating journal status
 */
data class UpdateJournalStatusRequest(
    val isOngoing: Boolean
)

/**
 * Request DTO for adding a journal entry
 */
data class AddJournalEntryRequest(
    val entryId: String
)

