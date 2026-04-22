package mk.ukim.finki.internshipmanagement.presentation.InternshipJournal.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.*
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.*
import org.antlr.v4.runtime.atn.ProfilingATNSimulator
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/internship-journal")
@Tag(
    name = "Internship Journal Commands",
    description = "API for creating and managing internship journal lifecycle. Create journals, update status, add entries, and complete internships."
)
class InternshipJournalCommandController(
    private val commandGateway: CommandGateway
) {

    @Operation(
        summary = "Create a new internship journal",
        description = "Creates a new internship journal for a student to document their internship experience with a company."
    )
    @PostMapping
    fun createInternshipJournal(
        @RequestBody request: CreateInternshipJournalRequest
    ): ResponseEntity<Map<String, String>> {
        val command = CreateInternshipJournalCommand(
            id = InternshipJournalId.generate(),
            companyName = CompanyName(request.companyName),
            studentId = StudentId(request.studentId),
            professorId = ProfessorId(request.professorId)
        )

        return try {
            commandGateway.sendAndWait<InternshipJournalId>(command)
            ResponseEntity.status(HttpStatus.CREATED)
                .body(mapOf("id" to command.id.getValue()))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("error" to (e.message ?: "Failed to create journal")))
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
            id = InternshipJournalId(id),
            isOngoing = request.isOngoing
        )

        return try {
            commandGateway.sendAndWait<Void>(command)
            ResponseEntity.ok(mapOf("message" to "Journal status updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("error" to (e.message ?: "Failed to update journal status")))
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
            journalId = InternshipJournalId(journalId),
            entryId = JournalEntryId(request.entryId)
        )

        return try {
            commandGateway.sendAndWait<Void>(command)
            ResponseEntity.ok(mapOf("message" to "Journal entry added successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("error" to (e.message ?: "Failed to add journal entry")))
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
            id = InternshipJournalId(id)
        )

        return try {
            commandGateway.sendAndWait<Void>(command)
            ResponseEntity.ok(mapOf("message" to "Internship journal completed successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("error" to (e.message ?: "Failed to complete journal")))
        }
    }
}

// Request DTOs
data class CreateInternshipJournalRequest(
    val companyName: String,
    val studentId: UUID,
    val professorId: UUID
)

data class UpdateJournalStatusRequest(
    val isOngoing: Boolean
)

data class AddJournalEntryRequest(
    val entryId: String
)

