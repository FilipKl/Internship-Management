package mk.ukim.finki.internshipmanagement.application.internshiprequest.eventhandler

import mk.ukim.finki.internshipmanagement.application.internshiprequest.query.InternshipRequestQueryService
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.events.InternshipRequestApprovedEvent
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class InternshipRequestEventHandler(
    val commandGateway: CommandGateway,
    val queryService: InternshipRequestQueryService
) {
    @EventHandler
    fun handle(event: InternshipRequestApprovedEvent) {
        // We look up the approved request using the read service
        // NOT the aggregate repository directly
        val request = queryService.findById(event.internshipRequestId)

        // sendAndWait is used here because the journal must be created
        // AFTER the request is confirmed approved - ordering matters
        // commandGateway.sendAndWait(
        //     OpenInternshipJournalCommand(
        //         studentId = request.studentId,
        //         internshipRequestId = request.internshipRequestId
        //     )
        // )
    }
}
