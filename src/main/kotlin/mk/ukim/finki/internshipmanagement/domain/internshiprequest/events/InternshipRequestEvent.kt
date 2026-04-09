package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events

import mk.ukim.finki.internshipmanagement.domain.common.AbstractEvent
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId

/**
 * Base class for all InternshipRequest events.
 * Extends AbstractEvent to support Kafka publishing.
 */
abstract class InternshipRequestEvent(
    open val internshipRequestId: InternshipRequestId
) : AbstractEvent(internshipRequestId)

