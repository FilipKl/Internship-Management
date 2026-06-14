package mk.ukim.finki.internshipmanagement.application.internshiprequest

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.SubmitInternshipRequestCommand
import mk.ukim.finki.internshipmanagement.infrastructure.client.StudentManagementClient
import mk.ukim.finki.internshipmanagement.application.internshiprequest.exception.StudentNotFoundException
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service

@Service
class SubmitInternshipRequestService(
    private val studentClient: StudentManagementClient,
    private val commandGateway: CommandGateway
) {

    fun submitWithValidation(command: SubmitInternshipRequestCommand) {
        if (!studentClient.existsStudent(command.studentId)) {
            throw StudentNotFoundException(command.studentId)
        }
        commandGateway.sendAndWait<Any>(command)
    }
}


