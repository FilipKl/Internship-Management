package mk.ukim.finki.internshipmanagement.application.InternshipJournal.exception

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId

/**
 * Thrown when InternshipJournal creation is attempted with a StudentId
 * that doesn't exist in the Student Management service.
 */
class StudentNotFoundException(val studentId: StudentId) :
    RuntimeException("Student with id $studentId not found in Student Management service")

