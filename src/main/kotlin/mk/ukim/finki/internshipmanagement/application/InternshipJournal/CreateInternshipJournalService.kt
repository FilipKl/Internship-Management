package mk.ukim.finki.internshipmanagement.application.InternshipJournal

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.CreateInternshipJournalCommand
import mk.ukim.finki.internshipmanagement.infrastructure.client.ProfessorManagementClient
import mk.ukim.finki.internshipmanagement.application.InternshipJournal.exception.ProfessorNotFoundException
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service

@Service
class CreateInternshipJournalService(
    private val professorClient: ProfessorManagementClient,
    private val commandGateway: CommandGateway
) {

    fun createWithValidation(command: CreateInternshipJournalCommand) {
        if (!professorClient.existsProfessor(command.professorId)) {
            throw ProfessorNotFoundException(command.professorId)
        }
        commandGateway.sendAndWait<Any>(command)
    }
}


