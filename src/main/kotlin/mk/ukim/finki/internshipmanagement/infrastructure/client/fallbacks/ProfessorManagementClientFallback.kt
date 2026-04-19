package mk.ukim.finki.internshipmanagement.infrastructure.client.fallbacks

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.ProfessorId
import mk.ukim.finki.internshipmanagement.infrastructure.client.ProfessorManagementClient
import org.springframework.stereotype.Component

/**
 * Fallback implementation for ProfessorManagementClient.
 *
 * This is invoked when:
 * 1. The Professor Management service is unreachable (circuit is OPEN)
 * 2. The connection times out
 * 3. Any HTTP error occurs
 *
 * Strategy: Return false (conservative default)
 * - If we can't reach the Professor service, we assume the professor doesn't exist
 * - This rejects the journal creation, which is safer than creating an invalid record
 * - When the service recovers, requests will succeed normally
 */
@Component
class ProfessorManagementClientFallback : ProfessorManagementClient {

    override fun existsProfessor(id: ProfessorId): Boolean {
        // Return false: assume professor doesn't exist if service is unreachable
        return false
    }
}

