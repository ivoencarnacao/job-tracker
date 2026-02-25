# ADR-0006: Flyway for Schema Migrations

## Status

Accepted

## Context

The database schema needs to evolve over time as features are added. Options for managing schema changes:

1. **Flyway** — SQL-based migration tool with versioned scripts.
2. **Liquibase** — XML/YAML/JSON-based migration tool with rollback support.
3. **Hibernate auto-DDL** — let the ORM generate and update the schema automatically from entity annotations.

## Decision

Use **Flyway** for database schema migrations with plain SQL scripts.

## Consequences

### Positive

- **Plain SQL migrations** — migration files are standard `.sql` files. No abstraction layer, no DSL to learn. Full access to PostgreSQL-specific features (check constraints, partial indexes, custom types).
- **Explicit schema control** — every schema change is a deliberate, versioned, reviewable file. No surprises from ORM-generated DDL.
- **Repeatable and deterministic** — Flyway tracks applied migrations in a metadata table. Running the same migrations on any environment produces the same schema.
- **Spring Boot integration** — Flyway runs automatically on application startup. No manual migration step needed.
- **Team-friendly** — SQL migrations are easy to review in pull requests. New team members can read the migration history to understand schema evolution.

### Negative

- **No automatic rollback** — Flyway Community Edition does not support undo migrations. Rolling back requires writing a new forward migration.
- **Manual SQL writing** — developers must write DDL by hand. No auto-generation from entity changes (unlike Hibernate auto-DDL or Liquibase diff).
- **Migration ordering** — in teams, concurrent branches creating migrations can cause version conflicts (mitigated by timestamp-based naming).

### Mitigation

- Hibernate is configured in **validation mode** (`spring.jpa.hibernate.ddl-auto=validate`), which checks that entity mappings match the schema at startup without modifying the database. This catches drift between entities and migrations early.
- Migration files use timestamp-based versioning (e.g., `V20260225_001__description.sql`) to reduce branch conflicts.
