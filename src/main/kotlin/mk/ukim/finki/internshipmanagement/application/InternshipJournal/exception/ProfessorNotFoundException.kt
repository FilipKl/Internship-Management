package mk.ukim.finki.internshipmanagement.application.InternshipJournal.exception

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.ProfessorId

/**
 * Thrown when InternshipJournal creation is attempted with a ProfessorId
 * that doesn't exist in the Professor Management service.
 */
class ProfessorNotFoundException(val professorId: ProfessorId) :
    RuntimeException("Professor with id $professorId not found in Professor Management service")

