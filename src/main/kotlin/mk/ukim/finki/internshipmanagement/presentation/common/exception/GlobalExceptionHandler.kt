package mk.ukim.finki.internshipmanagement.presentation.common.exception

import mk.ukim.finki.internshipmanagement.application.journalentry.exception.JournalEntryNotFoundException
import mk.ukim.finki.internshipmanagement.application.internshipposting.exception.InternshipPostingNotFoundException
import mk.ukim.finki.internshipmanagement.application.internshiprequest.exception.StudentNotFoundException
import mk.ukim.finki.internshipmanagement.application.InternshipJournal.exception.ProfessorNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

/**
 * Global exception handler for REST API.
 * Converts domain exceptions into appropriate HTTP responses.
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    
    @ExceptionHandler(JournalEntryNotFoundException::class)
    fun handleJournalEntryNotFound(
        ex: JournalEntryNotFoundException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val body = linkedMapOf<String, Any>(
            "timestamp" to LocalDateTime.now(),
            "status" to HttpStatus.NOT_FOUND.value(),
            "error" to "Journal Entry Not Found",
            "message" to (ex.message ?: "Not found"),
            "entryId" to ex.entryId,
            "path" to request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }
    
    @ExceptionHandler(InternshipPostingNotFoundException::class)
    fun handleInternshipPostingNotFound(
        ex: InternshipPostingNotFoundException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val body = linkedMapOf<String, Any>(
            "timestamp" to LocalDateTime.now(),
            "status" to HttpStatus.NOT_FOUND.value(),
            "error" to "Internship Posting Not Found",
            "message" to (ex.message ?: "Not found"),
            "postingId" to ex.postingId,
            "path" to request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }
    
    @ExceptionHandler(StudentNotFoundException::class)
    fun handleStudentNotFound(
        ex: StudentNotFoundException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val body = linkedMapOf<String, Any>(
            "timestamp" to LocalDateTime.now(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to "Student Not Found",
            "message" to (ex.message ?: "Student not found"),
            "studentId" to ex.studentId.toString(),
            "path" to request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(ProfessorNotFoundException::class)
    fun handleProfessorNotFound(
        ex: ProfessorNotFoundException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val body = linkedMapOf<String, Any>(
            "timestamp" to LocalDateTime.now(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to "Professor Not Found",
            "message" to (ex.message ?: "Professor not found"),
            "professorId" to ex.professorId.toString(),
            "path" to request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        ex: IllegalArgumentException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val body = linkedMapOf<String, Any>(
            "timestamp" to LocalDateTime.now(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to "Invalid Request",
            "message" to (ex.message ?: "Invalid argument"),
            "path" to request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }
    
    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<Any> {
        val body = linkedMapOf<String, Any>(
            "timestamp" to LocalDateTime.now(),
            "status" to HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "error" to "Internal Server Error",
            "message" to (ex.message ?: "An error occurred"),
            "path" to request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }
}

