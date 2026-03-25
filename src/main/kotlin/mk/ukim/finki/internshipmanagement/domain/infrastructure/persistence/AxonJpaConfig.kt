package mk.ukim.finki.internshipmanagement.domain.infrastructure.persistence

import jakarta.persistence.EntityManager
import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.jpa.SimpleEntityManagerProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonJpaConfig {

    @Bean
    @ConditionalOnMissingBean(EntityManagerProvider::class)
    fun entityManagerProvider(entityManager: EntityManager): EntityManagerProvider {
        return SimpleEntityManagerProvider(entityManager)
    }
}

