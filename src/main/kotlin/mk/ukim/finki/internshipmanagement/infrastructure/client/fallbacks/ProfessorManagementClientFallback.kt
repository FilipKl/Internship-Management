package mk.ukim.finki.internshipmanagement.infrastructure.client.fallbacks

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.ProfessorId
import mk.ukim.finki.internshipmanagement.infrastructure.client.ProfessorManagementClient
import org.springframework.stereotype.Component


@Component
class ProfessorManagementClientFallback : ProfessorManagementClient {

    override fun existsProfessor(id: ProfessorId): Boolean {
        return false
    }
}

