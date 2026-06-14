package mk.ukim.finki.internshipmanagement.infrastructure.client.fallbacks

import mk.ukim.finki.internshipmanagement.infrastructure.client.StudentManagementClient
import org.springframework.stereotype.Component


@Component
class StudentManagementClientFallback : StudentManagementClient {

    override fun existsStudent(id: mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId): Boolean {
        return false
    }
}

