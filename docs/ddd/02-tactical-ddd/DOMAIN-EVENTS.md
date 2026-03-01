# Domain Events

## Phasing

All domain events listed below are **P1 (Post-MVP)** scope. In the MVP, the Dashboard context uses **Open Host Service** (direct read-only queries against Tracking tables) instead of consuming events. Domain events will be introduced when the Skills context enters in P1 (VS-09/VS-10), as Skills needs to react to Tracking writes (`JobDescriptionUpdated`, `JobApplicationCreated`).

## Events

| Event | Record Fields | Publisher | Subscriber | Trigger |
|-------|---------------|-----------|------------|---------|
| `UserRegistered` | userId (UUID), email (String) | Identity | (analytics) | Successful registration |
| `JobApplicationCreated` | applicationId (UUID), userId (UUID), hasDescription (boolean) | Tracking | Skills (conditional extraction) | Application saved |
| `JobDescriptionUpdated` | applicationId (UUID), description (String) | Tracking | Skills (extraction) | Description saved/updated |
| `ApplicationStatusChanged` | applicationId (UUID), previousStatus (String), newStatus (String) | Tracking | Dashboard (metrics) | Status transition |
| `FollowUpScheduled` | applicationId (UUID), dueDate (LocalDate) | Tracking | Dashboard (pending follow-ups) | Follow-up created |
| `FollowUpCompleted` | applicationId (UUID), outcome (String) | Tracking | Dashboard (metrics) | Follow-up marked complete |
| `InterviewScheduled` | applicationId (UUID), scheduledAt (Instant), type (String) | Tracking | Dashboard (upcoming interviews) | Interview created |
| `OfferReceived` | applicationId (UUID), offerDate (LocalDate) | Tracking | Dashboard (offer tracking) | Offer recorded |

## Event Pattern (Clean Architecture)

```
Publishing Context:
  domain/event/EventName.java         → Plain Java record (no framework imports)
  application/DomainEventPublisher.java → Interface: void publish(Object event)
  infrastructure/SpringDomainEventPublisher.java → Spring implementation

Subscribing Context:
  infrastructure/listener/ListenerName.java → @EventListener method
```

## Rules

- Domain events are plain Java records — zero framework dependencies
- Events live in the publishing context's `domain/event/` package
- Subscribers live in the subscribing context's `infrastructure/listener/` package
- Only primitive data in events (UUIDs, Strings) — no domain objects cross boundaries
- Synchronous when introduced in P1 (same thread, same transaction)
