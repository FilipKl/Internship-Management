package mk.ukim.finki.internshipmanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InternshipManagementApplication

fun main(args: Array<String>) {
    runApplication<InternshipManagementApplication>(*args)
}