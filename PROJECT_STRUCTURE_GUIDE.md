# Internship Management - Project Structure 

## Overview
This project follows **Domain-Driven Design (DDD)** and **Command Query Responsibility Segregation (CQRS)** patterns. All aggregates must follow the same structure demonstrated with the `JournalEntry` aggregate.

---

## 📁 Project Directory Structure

```
src/main/kotlin/mk/ukim/finki/internshipmanagement/
├── domain/                          # Core business logic
│   ├── common/                      # Shared domain abstractions
│   │   ├── AggregateRoot.kt        # Base class for all aggregates
│   │   ├── DomainEvent.kt          # Base class for domain events
│   │   ├── Identifier.kt           # Base class for value object IDs
│   │   └── LabeledEntity.kt        # Base class for JPA entities
│   │
│   └── [AGGREGATE_NAME]/           # One folder per aggregate (e.g., journalentry)
│       ├── [AggregateName].kt      # Aggregate root entity
│       ├── [AggregateName]Id.kt    # Strongly-typed identifier (extends Identifier)
│       ├── ValueObject1.kt         # Value objects (e.g., EntryContent, WeekNumber)
│       ├── ValueObject2.kt
│       ├── commands/               # Command classes (what we want to do)
│       │   ├── Create[AggregateName]Command.kt
│       │   ├── Update[AggregateName]Command.kt
│       │   └── ...
│       └── events/                 # Domain event classes (what happened)
│           ├── [AggregateName]CreatedEvent.kt
│           ├── [AggregateName]UpdatedEvent.kt
│           └── ...
│
├── application/                     # Application services & use cases
│   └── [AGGREGATE_NAME]/
│       ├── eventhandler/           # Event handlers (reactions to domain events)
│       │   └── [AggregateName]EventHandler.kt
│       ├── query/                  # Query services & read model
│       │   ├── [AggregateName]View.kt          # Read model entity (@Immutable)
│       │   ├── [AggregateName]ViewRepository.kt # JPA repository for read model
│       │   └── [AggregateName]QueryService.kt  # Query service interface & impl
│       └── exception/              # Aggregate-specific exceptions
│           └── [AggregateName]NotFoundException.kt
│
├── infrastructure/                  # Technical infrastructure
│   ├── kafka/                       # Event streaming & cross-service communication
│   │   ├── KafkaEventConsumer.kt   # Kafka message listener (consumer)
│   │   ├── KafkaEventPublisher.kt  # Kafka event publisher (producer)
│   │   ├── externalDTOs.kt         # External DTOs from other services (ACL)
│   │   └── [EventName]Translator.kt # Anti-Corruption Layer (ACL) translator
│   │
│   ├── persistence/
│   │   ├── AxonJpaConfig.kt        # Axon Framework JPA configuration
│   │   └── [AggregateName]RepositoryConfig.kt  # GenericJpaRepository bean config
│   │
│   └── repository/                 # Repository implementations
│
├── presentation/                    # REST API & HTTP layer
│   ├── common/
│   │   └── exception/              # Global exception handling
│   │       └── GlobalExceptionHandler.kt
│   └── [AGGREGATE_NAME]/
│       └── controller/             # REST controllers (GET endpoints only)
│           └── [AggregateName]Controller.kt
│
└── InternshipManagementApplication.kt  # Spring Boot main application
```