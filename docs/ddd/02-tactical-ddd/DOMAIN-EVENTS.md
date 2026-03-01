# Domain Events

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
- Synchronous in MVP (same thread, same transaction)
