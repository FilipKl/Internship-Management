package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events

import mk.ukim.finki.internshipmanagement.domain.common.AbstractEvent
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId


abstract class InternshipRequestEvent(
    open val internshipRequestId: InternshipRequestId
) : AbstractEvent(internshipRequestId)

