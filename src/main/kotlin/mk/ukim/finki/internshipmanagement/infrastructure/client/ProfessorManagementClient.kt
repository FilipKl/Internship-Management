package mk.ukim.finki.internshipmanagement.infrastructure.client

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.ProfessorId
import mk.ukim.finki.internshipmanagement.infrastructure.client.fallbacks.ProfessorManagementClientFallback
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

/**
 * Feign HTTP client for calling the Professor Management service.
 * Provides declarative HTTP calls without boilerplate WebClient code.
 *
 * Service discovery is handled by Consul — no hardcoded URLs needed.
 * The name "professor-management" is used to query Consul for available instances.
 * When multiple instances exist, Spring Cloud's load balancer automatically
 * distributes requests across them.
 *
 * The circuit breaker is automatically managed via Resilience4j.
 * If the target service is down, the fallback is invoked immediately.
 */
@FeignClient(
    name = "professor-management",
    fallback = ProfessorManagementClientFallback::class
)
interface ProfessorManagementClient {

    /**
     * Check if a professor exists in the Professor Management service.
     *
     * @param id The professor ID (will be converted to string via toString())
     * @return true if the professor exists, false otherwise
     */
    @GetMapping("/professors/exists/{id}")
    fun existsProfessor(@PathVariable id: ProfessorId): Boolean
}

