package mk.ukim.finki.internshipmanagement.infrastructure.client.config

import mk.ukim.finki.internshipmanagement.infrastructure.client.ProfessorManagementClient
import mk.ukim.finki.internshipmanagement.infrastructure.client.StudentManagementClient
import org.axonframework.messaging.annotation.HandlerEnhancerDefinition
import org.axonframework.messaging.annotation.MessageHandlingMember
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy


@Configuration
class FeignClientBeanConfiguration {


    @Bean
    @Lazy
    fun ensureStudentManagementClientAvailable(@Autowired studentClient: StudentManagementClient): StudentManagementClient {
        return studentClient
    }

    @Bean
    @Lazy
    fun ensureProfessorManagementClientAvailable(@Autowired professorClient: ProfessorManagementClient): ProfessorManagementClient {
        return professorClient
    }
}


