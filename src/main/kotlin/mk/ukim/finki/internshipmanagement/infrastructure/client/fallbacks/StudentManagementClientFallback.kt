package mk.ukim.finki.internshipmanagement.infrastructure.client.fallbacks

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.StudentId
import mk.ukim.finki.internshipmanagement.infrastructure.client.StudentManagementClient
import org.springframework.stereotype.Component

/**
 * Fallback implementation for StudentManagementClient.
 *
 * This is invoked when:
 * 1. The Student Management service is unreachable (circuit is OPEN)
 * 2. The connection times out
 * 3. Any HTTP error occurs
 *
 * Strategy: Return false (conservative default)
 * - If we can't reach the Student service, we assume the student doesn't exist
 * - This rejects the internship request, which is safer than creating an invalid record
 * - When the service recovers, requests will succeed normally
 */
@Component
class StudentManagementClientFallback : StudentManagementClient {

    override fun existsStudent(id: StudentId): Boolean {
        // Return false: assume student doesn't exist if service is unreachable
        return false
    }
}

