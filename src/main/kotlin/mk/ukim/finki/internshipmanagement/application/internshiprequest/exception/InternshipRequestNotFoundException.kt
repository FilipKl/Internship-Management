package mk.ukim.finki.internshipmanagement.application.internshiprequest.exception

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId

class InternshipRequestNotFoundException(id: InternshipRequestId)
    : RuntimeException("Internship Request with id $id was not found")