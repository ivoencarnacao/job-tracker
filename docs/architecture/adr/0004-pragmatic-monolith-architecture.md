# ADR-0004: Pragmatic Monolith Architecture

## Status

Accepted

## Context

The application has four distinct functional areas (application tracking, skill analysis, dashboard metrics, user identity). These could be deployed as separate microservices or kept within a single application.

Options considered:

1. **Microservices** — each functional area as an independently deployable service.
2. **Modular monolith** — single deployment with enforced module boundaries (e.g., Java modules, ArchUnit).
3. **Pragmatic monolith** — single deployment with package-level boundaries and discipline.

## Decision

Use a **pragmatic monolith** — a single Spring Boot application with functional areas separated by top-level packages (bounded contexts). Boundaries are enforced by convention and code review, not by tooling.

## Consequences

### Positive

- **Simplest deployment model** — one JAR, one process, one database. No service discovery, no API gateways, no distributed transactions.
- **Fastest development cycle** — no inter-service communication overhead, no contract testing between services, no Docker Compose orchestration of multiple services.
- **Shared transaction boundary** — operations that span contexts (e.g., creating an application and extracting skills) are ACID-consistent without sagas or eventual consistency.
- **Easy refactoring** — renaming, moving code, and changing interfaces is an IDE operation, not a cross-repository coordination effort.
- **Right-sized for MVP** — the expected data volume and user count do not require horizontal scaling of individual components.

### Negative

- **Package boundaries are soft** — nothing prevents a developer from importing across context boundaries other than discipline.
- **Scaling is all-or-nothing** — cannot scale the skill extraction workload independently of the web layer.
- **Risk of coupling over time** — without enforcement, bounded contexts may degrade into a big ball of mud.

### Mitigation

- Package structure is documented in the bounded contexts specification.
- The clean separation (domain → application → infrastructure → web) within each context creates natural barriers.
- ArchUnit rules can be added later to enforce dependency rules at build time if coupling becomes a concern.
- The package boundaries established now make future extraction into modules or services straightforward.
