package mk.ukim.finki.internshipmanagement.application.internshiprequest.exception

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId


class StudentNotFoundException(val studentId: StudentId) :
    RuntimeException("Student with id $studentId not found in Student Management service")

