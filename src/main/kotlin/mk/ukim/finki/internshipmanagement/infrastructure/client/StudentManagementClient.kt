package mk.ukim.finki.internshipmanagement.infrastructure.client

import mk.ukim.finki.internshipmanagement.infrastructure.client.fallbacks.StudentManagementClientFallback
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@FeignClient(
    name = "student-management",
    fallback = StudentManagementClientFallback::class
)
interface StudentManagementClient {

    @GetMapping("/students/exists/{id}")
    fun existsStudent(@PathVariable id: mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId): Boolean
}

