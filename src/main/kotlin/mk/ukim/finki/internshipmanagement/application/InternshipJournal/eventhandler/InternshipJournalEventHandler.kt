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


    //TO DO: FIX WHEN MERGED IMPORT EVENTS FROM OTHER AGGREGATES.
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
  // Ordering matter cause if we used send we could have a situation where we are trying to add journal entries but we don't have a journal created.

    @EventHandler
    fun handle(event: JournalEntryAddedEvent) {

        val command = AddJournalEntryCommand(
            journalId = event.internshipJournalId,
            entryId = event.entryId
        )


            commandGateway.sendAndWait<Any>(command)



    }

    //InternshipJournalCreatedEvent whenever I create an internship journal, I want you to use this as a trigger to create your journal entries.
}

