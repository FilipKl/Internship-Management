package mk.ukim.finki.internshipmanagement.pact

import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.Pact
import au.com.dius.pact.model.RequestResponsePact
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.client.RestTemplate

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(providerName = "journal_entry_provider", hostInterface = "localhost")
class PactJournalEntryConsumerTest {

    @Pact(consumer = "internship_consumer")
    fun journalEntryByIdPact(builder: PactDslWithProvider): RequestResponsePact {
        @Suppress("UNCHECKED_CAST")
        return (builder
            .given("JournalEntry with ID JournalEntry:00000001 exists")
            .uponReceiving("A request to get journal entry by ID")
            .path("/api/v1/journal-entries/JournalEntry:00000001")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(mapOf("Content-Type" to "application/json;charset=UTF-8"))
            .body("""{
                "journalEntryId": {
                    "id": "00000000-0000-0000-0000-000000000001"
                },
                "journalId": "InternshipJournal:00000001",
                "title": "Week 1 Progress",
                "content": "Detailed week 1 content",
                "status": "DRAFT",
                "createdAt": "2026-05-20T14:00:00",
                "editedAt": null,
                "reviewedAt": null,
                "reviewedBy": null
            }""")
            .toPact()) as RequestResponsePact
    }

    @Test
    @PactTestFor(pactMethod = "journalEntryByIdPact", port = "9999")
    fun testJournalEntryById(mockServer: MockServer) {
        val response = RestTemplate()
            .getForEntity(mockServer.getUrl() + "/api/v1/journal-entries/JournalEntry:00000001", String::class.java)

        assert(response.statusCode.is2xxSuccessful)
        assert(response.body?.contains("Week 1 Progress") == true)
        assert(response.body?.contains("DRAFT") == true)
    }

    @Pact(consumer = "internship_consumer")
    fun journalEntriesByStatusPact(builder: PactDslWithProvider): RequestResponsePact {
        @Suppress("UNCHECKED_CAST")
        return (builder
            .given("JournalEntry with status DRAFT exists")
            .uponReceiving("A request to get journal entries by status")
            .path("/api/v1/journal-entries/by-status/DRAFT")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(mapOf("Content-Type" to "application/json;charset=UTF-8"))
            .body("""[{
                "journalEntryId": {
                    "id": "00000000-0000-0000-0000-000000000001"
                },
                "journalId": "InternshipJournal:00000001",
                "title": "Week 1 Entry",
                "content": "Week 1 content",
                "status": "DRAFT",
                "createdAt": "2026-05-20T14:00:00",
                "editedAt": null,
                "reviewedAt": null,
                "reviewedBy": null
            }]""")
            .toPact()) as RequestResponsePact
    }

    @Test
    @PactTestFor(pactMethod = "journalEntriesByStatusPact", port = "9999")
    fun testJournalEntriesByStatus(mockServer: MockServer) {
        val response = RestTemplate()
            .getForEntity(mockServer.getUrl() + "/api/v1/journal-entries/by-status/DRAFT", String::class.java)

        assert(response.statusCode.is2xxSuccessful)
        assert(response.body?.contains("DRAFT") == true)
    }
}


