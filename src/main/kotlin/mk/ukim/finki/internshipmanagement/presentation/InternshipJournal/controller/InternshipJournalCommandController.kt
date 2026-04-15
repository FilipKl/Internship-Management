package mk.ukim.finki.internshipmanagement.presentation.InternshipJournal.controller

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
class InternshipJournalCommandController(
    private val commandGateway: CommandGateway
) {

    /**
     * Create a new internship journal
     *
     * @param request CreateInternshipJournalRequest containing company name, student ID, and professor ID
     * @return ResponseEntity with the created journal ID
     */
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

    /**
     * Update the status of an internship journal (mark as ongoing or completed)
     *
     * @param id The journal ID
     * @param request UpdateJournalStatusRequest containing the new status
     * @return ResponseEntity indicating success or failure
     */
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

    /**
     * Add a journal entry to the internship journal
     *
     * @param journalId The journal ID
     * @param request AddJournalEntryRequest containing the entry ID
     * @return ResponseEntity indicating success or failure
     */
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

    /**
     * Complete an internship journal
     *
     * @param id The journal ID
     * @return ResponseEntity indicating success or failure
     */
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

