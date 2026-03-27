package mk.ukim.finki.internshipmanagement.infrastructure.persistence

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.InternshipJournal
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.InternshipJournalId
import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.eventhandling.EventBus
import org.axonframework.modelling.command.GenericJpaRepository
import org.axonframework.modelling.command.Repository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InternshipJournalRepositoryConfig {

    @Bean
    fun internshipJournalRepository(
        entityManagerProvider: EntityManagerProvider,
        eventBus: EventBus
    ): Repository<InternshipJournal> {

        return GenericJpaRepository.builder(InternshipJournal::class.java)
            .entityManagerProvider(entityManagerProvider)
            .eventBus(eventBus)
            .identifierConverter { id -> InternshipJournalId(id) }
            .build()
    }
}

