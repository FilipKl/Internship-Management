package mk.ukim.finki.internshipmanagement.infrastructure.client

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.StudentId
import mk.ukim.finki.internshipmanagement.infrastructure.client.fallbacks.StudentManagementClientFallback
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

/**
 * Feign HTTP client for calling the Student Management service.
 * Provides declarative HTTP calls without boilerplate WebClient code.
 *
 * Service discovery is handled by Consul — no hardcoded URLs needed.
 * The name "student-management" is used to query Consul for available instances.
 * When multiple instances exist, Spring Cloud's load balancer automatically
 * distributes requests across them.
 *
 * The circuit breaker is automatically managed via Resilience4j.
 * If the target service is down, the fallback is invoked immediately.
 */
@FeignClient(
    name = "student-management",
    fallback = StudentManagementClientFallback::class
)
interface StudentManagementClient {

    /**
     * Check if a student exists in the Student Management service.
     *
     * @param id The student ID (will be converted to string via toString())
     * @return true if the student exists, false otherwise
     */
    @GetMapping("/students/exists/{id}")
    fun existsStudent(@PathVariable id: StudentId): Boolean
}

