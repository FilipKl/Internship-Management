package mk.ukim.finki.internshipmanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients(basePackages = ["mk.ukim.finki.internshipmanagement.infrastructure.client"])
@EnableDiscoveryClient
class InternshipManagementApplication

fun main(args: Array<String>) {
    runApplication<InternshipManagementApplication>(*args)
}