package mk.ukim.finki.internshipmanagement.application.InternshipJournal

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.CreateInternshipJournalCommand
import mk.ukim.finki.internshipmanagement.infrastructure.client.ProfessorManagementClient
import mk.ukim.finki.internshipmanagement.application.InternshipJournal.exception.ProfessorNotFoundException
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service

/**
 * Application service for InternshipJournal creation with cross-service validation.
 *
 * This service validates the professor exists in the external Professor Management service
 * before creating the internship journal aggregate.
 */
@Service
class CreateInternshipJournalService(
    private val professorClient: ProfessorManagementClient,
    private val commandGateway: CommandGateway
) {

    /**
     * Create an internship journal with professor validation.
     *
     * @param command The command to create the internship journal
     * @throws ProfessorNotFoundException if the referenced professor doesn't exist
     */
    fun createWithValidation(command: CreateInternshipJournalCommand) {
        // Validate: does the referenced professor exist in the Professor service?
        if (!professorClient.existsProfessor(command.professorId)) {
            throw ProfessorNotFoundException(command.professorId)
        }

        // Validation passed - send the command to create the aggregate
        commandGateway.sendAndWait<Any>(command)
    }
}


