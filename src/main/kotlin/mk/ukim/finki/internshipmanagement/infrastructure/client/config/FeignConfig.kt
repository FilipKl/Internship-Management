package mk.ukim.finki.internshipmanagement.infrastructure.client.config

import mk.ukim.finki.internshipmanagement.infrastructure.client.interceptor.CorrelationIdInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import feign.RequestInterceptor


@Configuration
class FeignConfig {

    @Bean
    fun correlationIdInterceptor(): RequestInterceptor {
        return CorrelationIdInterceptor()
    }
}

