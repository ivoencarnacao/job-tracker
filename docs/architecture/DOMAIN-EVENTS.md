# Cross-Context Communication (Domain Events)

## Events

| Event                      | Publisher | Subscriber                             | Trigger                          |
| -------------------------- | --------- | -------------------------------------- | -------------------------------- |
| `JobDescriptionUpdated`    | Tracking  | Skills (extraction)                    | Job description saved or updated |
| `ApplicationStatusChanged` | Tracking  | Dashboard (future: cache invalidation) | Pipeline status transition       |

## Pattern

Bounded contexts communicate exclusively via Spring Application Events, following Clean Architecture:

```
domain/event/
  JobDescriptionUpdated.java          # Plain Java record (no framework imports)
  ApplicationStatusChanged.java       # Plain Java record

application/
  DomainEventPublisher.java           # Interface: void publish(Object event)

infrastructure/
  SpringDomainEventPublisher.java     # Implements DomainEventPublisher using
                                      #   Spring's ApplicationEventPublisher

infrastructure/listener/
  SkillExtractionListener.java        # @EventListener subscribing to
                                      #   JobDescriptionUpdated
```

### Example Flow

1. `CreateJobApplicationUseCase` saves a JobApplication with a description
2. Use case calls `domainEventPublisher.publish(new JobDescriptionUpdated(appId, description))`
3. `SpringDomainEventPublisher` delegates to Spring's `ApplicationEventPublisher`
4. `SkillExtractionListener` in Skills context receives the event
5. Listener calls `ExtractSkillsUseCase` with the application ID and description text

### Rules

- Domain events are plain Java records — zero framework dependencies
- Events live in the **publishing** context's `domain/event/` package
- Subscribers live in the **subscribing** context's `infrastructure/listener/` package
- Only primitive data in events (UUIDs, Strings) — no domain objects cross boundaries
- Synchronous in MVP (same thread, same transaction)
- No context imports another context's domain model
