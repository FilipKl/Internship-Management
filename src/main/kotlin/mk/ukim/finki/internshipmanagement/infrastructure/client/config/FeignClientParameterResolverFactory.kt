package mk.ukim.finki.internshipmanagement.infrastructure.client.config

import mk.ukim.finki.internshipmanagement.infrastructure.client.ProfessorManagementClient
import mk.ukim.finki.internshipmanagement.infrastructure.client.StudentManagementClient
import org.axonframework.messaging.annotation.HandlerEnhancerDefinition
import org.axonframework.messaging.annotation.MessageHandlingMember
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

/**
 * Configuration to ensure Feign clients are available as Spring beans for Axon injection.
 *
 * This configuration ensures that when Axon tries to instantiate command handlers,
 * the Feign client beans (StudentManagementClient, ProfessorManagementClient) are
 * properly available and can be autowired by Spring's dependency resolution mechanism.
 */
@Configuration
class FeignClientBeanConfiguration {

    /**
     * Explicitly declare StudentManagementClient bean availability.
     * This ensures Spring resolves it before Axon tries to instantiate aggregates.
     */
    @Bean
    @Lazy
    fun ensureStudentManagementClientAvailable(@Autowired studentClient: StudentManagementClient): StudentManagementClient {
        return studentClient
    }

    /**
     * Explicitly declare ProfessorManagementClient bean availability.
     * This ensures Spring resolves it before Axon tries to instantiate aggregates.
     */
    @Bean
    @Lazy
    fun ensureProfessorManagementClientAvailable(@Autowired professorClient: ProfessorManagementClient): ProfessorManagementClient {
        return professorClient
    }
}


