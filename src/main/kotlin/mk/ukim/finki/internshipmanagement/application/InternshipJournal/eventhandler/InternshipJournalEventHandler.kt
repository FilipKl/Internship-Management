package mk.ukim.finki.internshipmanagement.application.InternshipJournal.eventhandler

import mk.ukim.finki.internshipmanagement.application.InternshipJournal.query.InternshipJournalQueryService

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.AddJournalEntryCommand
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.commands.CreateInternshipJournalCommand
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.events.InternshipJournalCreatedEvent
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.events.JournalEntryAddedEvent
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class IncomingEventHandler(
    private val commandGateway: CommandGateway,
    private val InternshipJournalQueryService: InternshipJournalQueryService,
) {


    @EventHandler
    fun on(event: InternshipJournalCreatedEvent) {

        val command = CreateInternshipJournalCommand(
            id = event.internshipJournalId,
            companyName = event.companyName,
            studentId = event.studentId,
            professorId = event.professorId
        )

        commandGateway.sendAndWait<Any>(command)
    }

    @EventHandler
    fun handle(event: JournalEntryAddedEvent) {

        val command = AddJournalEntryCommand(
            journalId = event.internshipJournalId,
            entryId = event.entryId
        )


            commandGateway.sendAndWait<Any>(command)



    }

}

