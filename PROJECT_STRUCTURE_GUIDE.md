# Internship Management - Project Structure

```
src/main/kotlin/mk/ukim/finki/internshipmanagement/
├── domain/                                  # Core business logic
│   ├── common/                              # Shared abstractions
│   │   ├── AbstractEvent.kt                # Base class for ALL events (Kafka-enabled)
│   │   ├── AggregateRoot.kt                # Base class for aggregates
│   │   ├── DomainEvent.kt                  # Domain events base
│   │   ├── Identifier.kt                   # Value object IDs
│   │   └── LabeledEntity.kt                # JPA entities base
│   │
│   └── [AGGREGATE_NAME]/                   # One folder per aggregate (InternshipJournal, journalentry, etc.)
│       ├── [AggregateName].kt              # Aggregate root entity
│       ├── [AggregateName]Id.kt            # Strongly-typed identifier
│       ├── ValueObject1.kt, ValueObject2.kt # Value objects
│       ├── commands/
│       │   └── [AggregateName]Commands.kt  # Create, Update, Delete commands
│       └── events/
│           └── [AggregateName]Events.kt    # Domain events with toExternalEvent()
│
├── application/                             # Business logic & services
│   └── [AGGREGATE_NAME]/
│       ├── [AggregateName]Service.kt       # Application service (some have cross-service validation)
│       ├── eventhandler/
│       │   ├── [AggregateName]EventHandler.kt       # Handles events from same aggregate
│       │   └── [AggregateName]ViewEventHandler.kt   # Updates read model
│       ├── query/
│       │   ├── [AggregateName]View.kt
│       │   ├── [AggregateName]ViewRepository.kt
│       │   └── [AggregateName]QueryService.kt
│       └── exception/
│           └── [AggregateName]NotFoundException.kt
│
├── infrastructure/                          # Technical infrastructure
│   ├── client/                              # Feign HTTP Clients
│   │   ├── StudentManagementClient.kt       # GET /students/exists/{id}
│   │   ├── ProfessorManagementClient.kt     # GET /professors/exists/{id}
│   │   ├── config/
│   │   │   ├── FeignConfig.kt
│   │   │   └── FeignClientParameterResolverFactory.kt
│   │   ├── fallbacks/                       # Circuit breaker fallbacks
│   │   │   ├── StudentManagementClientFallback.kt
│   │   │   └── ProfessorManagementClientFallback.kt
│   │   └── interceptor/
│   │       └── CorrelationIdInterceptor.kt
│   │
│   ├── messaging/                           # Kafka publishing (layered)
│   │   ├── EventMessagingService.kt         # Interface (hides Kafka)
│   │   ├── EventMessagingRepository.kt      # Repository interface
│   │   ├── impl/
│   │   │   ├── EventMessagingServiceImpl.kt
│   │   │   └── KafkaMessagingRepositoryImpl.kt  # Only Kafka-aware class
│   │   └── handlers/
│   │       └── EventMessagingEventHandler.kt # Generic handler for ALL events
│   │
│   ├── kafka/
│   │   ├── KafkaConfig.kt
│   │   ├── KafkaEventConsumer.kt
│   │   └── acl/
│   │       ├── ExternalDTOs.kt
│   │       └── PartnerEventTranslator.kt
│   │
│   ├── persistence/
│   │   ├── AxonJpaConfig.kt
│   │   ├── AxonRepositoryConfig.kt
│   │   └── InternshipJournalRepositoryConfig.kt
│   │
│   └── repository/
│       └── RepositoryConfiguration.kt
│
├── presentation/                            # REST API & HTTP
│   ├── common/
│   │   └── exception/
│   │       └── GlobalExceptionHandler.kt    # Centralized error handling
│   │
│   ├── [AGGREGATE_NAME]/
│   │   └── controller/
│   │       ├── [AggregateName]CommandController.kt  # POST/PUT: commands (create, update, delete)
│   │       └── [AggregateName]Controller.kt         # GET: queries (read model)
│   │
│   └── kafka/controller/
│       └── KafkaManagementController.kt    # Kafka admin/debugging endpoints
│
└── InternshipManagementApplication.kt       # @SpringBootApplication
```