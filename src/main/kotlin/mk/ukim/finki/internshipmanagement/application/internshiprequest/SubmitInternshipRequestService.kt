package mk.ukim.finki.internshipmanagement.application.internshiprequest

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.SubmitInternshipRequestCommand
import mk.ukim.finki.internshipmanagement.infrastructure.client.StudentManagementClient
import mk.ukim.finki.internshipmanagement.application.internshiprequest.exception.StudentNotFoundException
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Service

/**
 * Application service for InternshipRequest creation with cross-service validation.
 *
 * This service validates the student exists in the external Student Management service
 * before creating the internship request aggregate.
 */
@Service
class SubmitInternshipRequestService(
    private val studentClient: StudentManagementClient,
    private val commandGateway: CommandGateway
) {

    /**
     * Submit an internship request with student validation.
     *
     * @param command The command to create the internship request
     * @throws StudentNotFoundException if the referenced student doesn't exist
     */
    fun submitWithValidation(command: SubmitInternshipRequestCommand) {
        // Validate: does the referenced student exist in the Student service?
        if (!studentClient.existsStudent(command.studentId)) {
            throw StudentNotFoundException(command.studentId)
        }

        // Validation passed - send the command to create the aggregate
        commandGateway.sendAndWait<Any>(command)
    }
}


