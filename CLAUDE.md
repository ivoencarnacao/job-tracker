# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Job application tracking system (MVP) built with Spring Boot 3.5.11, Java 21, PostgreSQL 17, Thymeleaf, Vite, and Tailwind CSS 4. The architecture follows Domain-Driven Design (DDD). See `PRD.md` for full product requirements.

## Build & Run Commands

```bash
# Build
./mvnw clean package

# Run (local development - requires Docker for PostgreSQL)
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Start Vite dev server (separate terminal, for CSS live reload)
npm run dev

# Run unit tests only
./mvnw test

# Run all tests (unit + integration tests ending in *IT.java)
./mvnw verify

# Run a single test class
./mvnw test -Dtest=SomeClassTests

# Format Java code (Spring Java Format)
./mvnw spring-javaformat:apply

# Format frontend files (Prettier)
npx prettier --write "src/main/resources/**/*.{html,css,js,json}"
```

## Architecture

- **Base package**: `dev.ivoencarnacao.jobtracker`
- **Database**: PostgreSQL 17 via Docker Compose, schema `core`, managed by Flyway migrations in `src/main/resources/db/migration/`
- **Enums**: Use PostgreSQL native enum types (`CREATE TYPE`). Map in JPA with `@Enumerated(EnumType.STRING)` + Hibernate 6 `PostgreSQLEnumJdbcType`. Never store enums as plain `VARCHAR`.
- **ORM**: Spring Data JPA with Hibernate (validation mode, no DDL auto-update)
- **Mapping**: MapStruct 1.6.3 for DTO/entity conversion
- **Templates**: Thymeleaf with layout dialect, served from `src/main/resources/templates/`
- **Frontend assets**: Vite builds `src/main/resources/static/css/application.css` (Tailwind entry point) into `target/classes/static`
- **Security**: Spring Security (local profile uses admin/admin)

## Clean Architecture

The codebase follows Clean Architecture with DDD Bounded Contexts. Each bounded context is a top-level package under `dev.ivoencarnacao.jobtracker` with four layers:

- **domain/** — Entities, aggregates, value objects, domain services, repository interfaces. Zero framework dependencies. This is the innermost layer.
- **application/** — Use cases, input/output DTOs, service interfaces. Depends only on `domain`.
- **infrastructure/** — JPA repository implementations, MapStruct mappers, framework configurations. Depends on `domain` and `application`.
- **web/** — Controllers, request/response DTOs, view models. Depends on `application`.

**Dependency rule:** Dependencies point inward only. `domain` has no dependencies on other layers. `application` depends only on `domain`. Outer layers (`infrastructure`, `web`) depend on inner layers but never the reverse.

**Bounded Contexts:**

- `identity/` — User management, authentication, registration
- `tracking/` — Job application lifecycle, interviews, follow-ups, offers
- `skills/` — Skill catalog, extraction, normalization, trend analysis
- `dashboard/` — Read-only query services, metrics, insights
- `shared/config/` — Cross-cutting configuration (web, security, exception handling)

**Do NOT** use Hexagonal Architecture terminology (ports, adapters) — use Clean Architecture terms (interfaces, implementations, use cases).

**Strict DDD Aggregates:** All write operations on child entities must go through the aggregate root. No separate write repositories for child entities. Read-only query repositories are allowed for cross-aggregate queries (dashboards, lists).

**Cross-Context Communication via Spring Events:** Bounded contexts communicate exclusively via Spring Application Events. Domain events are plain Java records in the publishing context's `domain/event/` package. Publishing uses a `DomainEventPublisher` interface (application layer) implemented by `SpringDomainEventPublisher` (infrastructure layer). Subscribers use `@EventListener` in their `infrastructure/listener/` package. No direct imports of another context's domain model — correlation via primitive IDs only.

## Profiles

- `local` — Development: Vite dev mode, Thymeleaf cache off, SQL logging, Flyway enabled
- `test` — Testcontainers with PostgreSQL 17, DEBUG logging for services and SQL
- Default — Production/Docker

## Testing Conventions

- Unit tests: `*Tests.java` (Maven Surefire)
- Integration tests: `*IT.java` (Maven Failsafe)
- Testcontainers with `init-schemas.sql` bootstraps the `core` schema
- JaCoCo enforces minimum 70% line coverage on `verify`

## Code Style

- **Java**: Spring Java Format (4-space indent). Validated at build time; fix with `./mvnw spring-javaformat:apply`
- **HTML/CSS/JS**: Prettier with Tailwind plugin (2-space indent)
- **Line endings**: LF (enforced via `.editorconfig`)

## Infrastructure

Docker Compose (`compose.yaml`) provides PostgreSQL 17 and pgAdmin 4. Spring Boot's Docker Compose support auto-starts containers when running locally.

## Git Conventions

Always use **Conventional Commits** with small, meaningful commits. Format: `type(scope): description`

Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`, `build`, `ci`, `perf`

Subject line only — **no body or footer**. Keep it concise and self-explanatory.

**Commit by context:** Split commits by bounded context or concern. One commit per logical change — do not bundle unrelated changes together.

**No Co-Authored-By:** Never add `Co-Authored-By` trailers to commit messages.
