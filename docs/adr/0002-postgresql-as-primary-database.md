# ADR-0002: PostgreSQL as Primary Database

## Status

Accepted

## Context

The application needs a reliable relational database for structured data (applications, interviews, follow-ups, offers) with support for complex queries (dashboard metrics, skill frequency analysis, filtering, and sorting).

Options considered:

1. **PostgreSQL** — full-featured open-source relational database.
2. **MySQL** — widely used open-source relational database.
3. **H2** — embedded Java database (in-memory or file-based).
4. **MongoDB** — document-oriented NoSQL database.

## Decision

Use **PostgreSQL 17** as the primary and only database.

## Consequences

### Positive

- **Rich query capabilities** — window functions, CTEs, JSON operators, and full-text search support complex dashboard and analytics queries without application-level workarounds.
- **Strong data integrity** — foreign keys, check constraints, and transactional guarantees match the domain's relational nature (applications have interviews, follow-ups, offers).
- **Excellent Spring Data JPA support** — Hibernate's PostgreSQL dialect is mature and well-tested.
- **Production-ready** — handles the expected data volumes (hundreds to thousands of applications per user) with room to grow.
- **Flyway compatibility** — first-class migration support for PostgreSQL.
- **Docker Compose integration** — Spring Boot auto-starts PostgreSQL via Docker Compose for local development, minimizing setup friction.

### Negative

- **Requires Docker for local development** — developers need Docker installed to run the app locally (no embedded fallback).
- **Heavier than H2 for testing** — integration tests are slower than in-memory alternatives (mitigated by Testcontainers).
- **Operational overhead in production** — requires managed hosting or self-managed instance (connection pooling, backups, monitoring).

### Mitigation

- Docker Compose provides zero-config local setup.
- Testcontainers ensures integration tests run against the same database engine as production, catching dialect-specific issues early.
