package mk.ukim.finki.internshipmanagement.application.InternshipJournal.exception

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.ProfessorId


class ProfessorNotFoundException(val professorId: ProfessorId) :
    RuntimeException("Professor with id $professorId not found in Professor Management service")

