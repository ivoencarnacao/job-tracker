# Future Technical Improvements

Technical improvements beyond MVP scope, to consider in future iterations.

> Product features (skill trends, recruiter management, CSV export) are tracked in [product/backlog/FUTURE.md](../product/backlog/FUTURE.md).

## Spring Events (P1 — Skills Context)

Domain events are deferred from MVP to P1. In the MVP, the Dashboard uses Open Host Service (direct read-only queries). Events will be introduced when the Skills context enters (VS-09/VS-10), as Skills needs to react to Tracking writes.

**Trigger:** Begin implementation when starting VS-09 (Skill Catalog + Manual Tagging) or VS-10 (Skill Extraction Engine).

### Events to Implement

| Event | Record Fields | Publisher | Subscriber |
|-------|---------------|-----------|------------|
| `JobApplicationCreated` | applicationId (UUID), userId (UUID), hasDescription (boolean) | Tracking | Skills |
| `JobDescriptionUpdated` | applicationId (UUID), description (String) | Tracking | Skills |
| `ApplicationStatusChanged` | applicationId (UUID), previousStatus (String), newStatus (String) | Tracking | Dashboard (optional) |
| `UserRegistered` | userId (UUID), email (String) | Identity | (analytics) |
| `FollowUpScheduled` | applicationId (UUID), dueDate (LocalDate) | Tracking | Dashboard (optional) |
| `FollowUpCompleted` | applicationId (UUID), outcome (String) | Tracking | Dashboard (optional) |
| `InterviewScheduled` | applicationId (UUID), scheduledAt (Instant), type (String) | Tracking | Dashboard (optional) |
| `OfferReceived` | applicationId (UUID), offerDate (LocalDate) | Tracking | Dashboard (optional) |

### Implementation Checklist

1. **Tracking context (publisher):**
   - [ ] Create event records in `tracking/domain/event/` (plain Java records, zero framework deps)
   - [ ] Create `DomainEventPublisher` interface in `tracking/application/`
   - [ ] Create `SpringDomainEventPublisher` in `tracking/infrastructure/` (delegates to Spring `ApplicationEventPublisher`)
   - [ ] Add `eventPublisher.publish(...)` calls in relevant use cases

2. **Skills context (subscriber):**
   - [ ] Create `SkillExtractionListener` in `skills/infrastructure/listener/` with `@EventListener`
   - [ ] Wire listener to call `ExtractSkillsUseCase`

3. **Dashboard context (optional migration):**
   - [ ] Dashboard can continue using Open Host Service (direct queries)
   - [ ] Optionally add event listeners for cache invalidation or real-time metric updates

### Post-P1 Event Evolution

- **Async domain events:** Add `@Async` + `@EnableAsync` for non-blocking event processing
- **Post-commit events:** Use `@TransactionalEventListener(phase = AFTER_COMMIT)` to ensure events fire only after successful transaction commit

## Infrastructure

- **Optimistic locking:** Add version column for concurrent edit detection (multiple tabs scenario)
- **Full-text search:** PostgreSQL `tsvector`/`tsquery` for richer search capabilities
