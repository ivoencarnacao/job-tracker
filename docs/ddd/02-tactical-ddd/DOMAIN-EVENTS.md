# Domain Events

## Events

| Event | Record Fields | Publisher | Subscriber | Trigger |
|-------|---------------|-----------|------------|---------|
| `JobDescriptionUpdated` | applicationId (UUID), description (String) | Tracking | Skills (extraction) | Description saved/updated |
| `ApplicationStatusChanged` | applicationId (UUID), previousStatus (String), newStatus (String) | Tracking | Dashboard (future) | Status transition |

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
