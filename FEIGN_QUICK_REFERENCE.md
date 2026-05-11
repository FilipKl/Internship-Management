# Feign Client Injection - Quick Reference

## Pattern: Inject Feign Client into @CommandHandler

### Basic Example
```kotlin
@CommandHandler
fun handle(
    command: YourCommand,
    externalService: ExternalServiceClient  // Axon injects this
) {
    // Validate before applying event
    if (!externalService.validateResource(command.resourceId)) {
        throw ResourceNotFoundException(command.resourceId)
    }
    
    // Validation passed
    apply(YourEventCreated(command))
}
```

---

## InternshipJournal Implementation

### CreateInternshipJournal - 2 Validations (Professor + Student)

```kotlin
@CommandHandler
constructor(
    command: CreateInternshipJournalCommand,
    professorClient: ProfessorManagementClient,      // Injected
    studentClient: StudentManagementClient           // Injected
) : super() {
    if (!professorClient.existsProfessor(command.professorId)) {
        throw ProfessorNotFoundException(command.professorId)
    }
    if (!studentClient.existsStudent(command.studentId)) {
        throw StudentNotFoundException(command.studentId)
    }
    apply(InternshipJournalCreatedEvent(command))
}
```

---

## Key Points

1. **Parameter Position:** Feign clients come as parameters AFTER the command
2. **Timing:** Validation happens INSIDE the handler BEFORE `apply(event)`
3. **Exceptions:** Throw typed exceptions on validation failure
4. **No State:** Clients are NOT class fields, they're method parameters only
5. **Axon Magic:** Axon automatically injects Spring beans into handler parameters

---

## Files to Check

- **Aggregate:** `domain/InternshipJournal/InternshipJournal.kt`
- **Exceptions:** `application/InternshipJournal/exception/`
- **Clients:** `infrastructure/client/`
- **Implementation Details:** `FEIGN_INJECTION_IMPLEMENTATION.md`

---

## Test Commands

```bash
# Create journal (will validate professor and student)
curl -X POST http://localhost:8086/api/v1/internship-journals \
  -H 'Content-Type: application/json' \
  -d '{"companyName":"Test","studentId":"Student:123","professorId":"Professor:456"}'
```



