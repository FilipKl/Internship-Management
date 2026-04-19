package mk.ukim.finki.internshipmanagement.infrastructure.client.config

import mk.ukim.finki.internshipmanagement.infrastructure.client.interceptor.CorrelationIdInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import feign.RequestInterceptor

/**
 * Spring Configuration class for Feign client setup.
 *
 * This configuration class registers beans that customize Feign behavior:
 * - Request Interceptors: Modify outgoing requests (e.g., add headers)
 * - Decoders/Encoders: Parse responses and serialize requests
 * - Error Handlers: Handle specific error scenarios
 */
@Configuration
class FeignConfig {

    /**
     * Register the Correlation ID Interceptor as a Spring bean.
     *
     * Once registered, Feign automatically uses this interceptor for ALL
     * outgoing HTTP requests from any Feign client in the application.
     * No need to wire it into each client individually.
     *
     * @return A new CorrelationIdInterceptor instance
     */
    @Bean
    fun correlationIdInterceptor(): RequestInterceptor {
        return CorrelationIdInterceptor()
    }
}

