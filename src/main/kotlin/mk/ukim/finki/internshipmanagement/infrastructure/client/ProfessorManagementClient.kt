package mk.ukim.finki.internshipmanagement.infrastructure.client

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.ProfessorId
import mk.ukim.finki.internshipmanagement.infrastructure.client.fallbacks.ProfessorManagementClientFallback
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@FeignClient(
    name = "professor-management",
    fallback = ProfessorManagementClientFallback::class
)
interface ProfessorManagementClient {

    @GetMapping("/professors/exists/{id}")
    fun existsProfessor(@PathVariable id: ProfessorId): Boolean
}

