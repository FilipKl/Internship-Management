# Internship Management - Project Structure 

---

## 📁 Project Directory Structure

```
src/main/kotlin/mk/ukim/finki/internshipmanagement/
├── domain/                          # Core business logic
│   ├── common/                      # Shared domain abstractions
│   │   ├── AbstractEvent.kt        # Base class for ALL events (Kafka-enabled)
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
│           ├── [AggregateName]Event.kt          # Base event class (extends AbstractEvent)
│           ├── [AggregateName]CreatedEvent.kt  # Publishable event with toExternalEvent()
│           ├── [AggregateName]CreatedExternalEvent.kt  # External contract
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
│   ├── messaging/                   # Event publishing to Kafka (layered)
│   │   ├── EventMessagingService.kt       # Service interface (hides Kafka)
│   │   ├── EventMessagingRepository.kt    # Repository interface
│   │   ├── impl/
│   │   │   ├── EventMessagingServiceImpl.kt      # Service implementation
│   │   │   └── KafkaMessagingRepositoryImpl.kt   # ONLY class with KafkaTemplate
│   │   └── handlers/
│   │       └── EventMessagingEventHandler.kt    # Generic handler for ALL events
│   │
│   ├── kafka/                       # Event streaming & cross-service communication
│   │   ├── KafkaConfig.kt          # Kafka beans & configuration
│   │   ├── KafkaEventConsumer.kt   # Kafka message listener (consumer)
│   │   ├── acl/                    # Anti-Corruption Layer (ACL)
│   │   │   ├── ExternalDTOs.kt     # External DTOs from other services (defensive)
│   │   │   └── PartnerEventTranslator.kt  # ACL translator (DTO → domain)
│   │   └── KafkaEventPublisher.kt  # Legacy (replaced by EventMessagingEventHandler)
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
