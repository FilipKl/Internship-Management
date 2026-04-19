package mk.ukim.finki.internshipmanagement.infrastructure.client.interceptor

import feign.RequestInterceptor
import feign.RequestTemplate
import java.util.*

/**
 * Feign Request Interceptor that attaches a unique X-Correlation-ID header
 * to every outgoing HTTP request.
 *
 * The Correlation ID enables request tracing across all microservices.
 * When a service calls another service, it passes the same Correlation ID,
 * creating a traceable chain in logs across the entire system.
 *
 * Flow:
 * 1. External request arrives at this service (with or without X-Correlation-ID)
 * 2. Feign makes an outgoing call to another service
 * 3. This interceptor checks if X-Correlation-ID is already present
 * 4. If not present: generates a new UUID as the Correlation ID
 * 5. If present: reuses the existing ID to maintain the trace chain
 * 6. The header is added to the request template
 * 7. Receiving service sees the same Correlation ID in its logs
 */
class CorrelationIdInterceptor : RequestInterceptor {

    override fun apply(requestTemplate: RequestTemplate) {
        // Check if the correlation ID already exists (passed from upstream service)
        if (!requestTemplate.headers().containsKey("X-Correlation-ID")) {
            // Generate a new correlation ID for this trace chain
            val correlationId = UUID.randomUUID().toString()
            requestTemplate.header("X-Correlation-ID", correlationId)
        }
    }
}

