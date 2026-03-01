# ADR-0007: Testcontainers for Integration Tests

## Status

Accepted

## Context

Integration tests need a database to verify repository queries, migration scripts, and end-to-end flows. Options:

1. **H2 in-memory database** — fast, zero-setup, runs in the JVM process.
2. **Embedded PostgreSQL** — PostgreSQL binary started by the test framework.
3. **Testcontainers** — Docker-based ephemeral containers started per test suite.

## Decision

Use **Testcontainers** with a PostgreSQL 17 Docker container for all integration tests.

## Consequences

### Positive

- **Production parity** — tests run against the exact same database engine, version, and SQL dialect as production. No dialect mismatches (H2 does not support many PostgreSQL-specific features like `gen_random_uuid()`, `TIMESTAMPTZ`, check constraints with specific syntax, etc.).
- **Flyway validation** — migration scripts execute against real PostgreSQL, catching syntax errors and incompatibilities that H2 would silently accept or reject differently.
- **Isolation** — each test suite gets a fresh container. No shared state, no manual cleanup, no flaky tests from leftover data.
- **Spring Boot support** — `@ServiceConnection` annotation auto-configures the DataSource from the Testcontainers instance. Minimal boilerplate.
- **Reusable containers** — Testcontainers can reuse a running container across test suites in the same build, reducing startup overhead.

### Negative

- **Requires Docker** — developers and CI environments must have Docker available. Tests fail without it.
- **Slower than H2** — container startup adds 2-5 seconds per test suite (mitigated by container reuse).
- **Resource consumption** — running PostgreSQL in Docker uses more memory and CPU than an in-memory database.

### Mitigation

- Container reuse (`testcontainers.reuse.enable=true`) keeps a single PostgreSQL container running across test suites, amortizing the startup cost.
- An `init-schemas.sql` script bootstraps the `core` schema on container creation, matching the production schema layout.
- CI pipelines (GitHub Actions, GitLab CI) natively support Docker, so no additional setup is needed.
