package mk.ukim.finki.internshipmanagement.infrastructure.client.interceptor

import feign.RequestInterceptor
import feign.RequestTemplate
import java.util.*


class CorrelationIdInterceptor : RequestInterceptor {

    override fun apply(requestTemplate: RequestTemplate) {
        if (!requestTemplate.headers().containsKey("X-Correlation-ID")) {
            val correlationId = UUID.randomUUID().toString()
            requestTemplate.header("X-Correlation-ID", correlationId)
        }
    }
}

