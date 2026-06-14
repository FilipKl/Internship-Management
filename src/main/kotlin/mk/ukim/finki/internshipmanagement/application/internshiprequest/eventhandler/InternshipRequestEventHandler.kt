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
        val request = queryService.findById(event.internshipRequestId)
    }
}
