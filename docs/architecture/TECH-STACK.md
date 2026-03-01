# Technology Stack

## Architecture Overview

Job Tracker is a **pragmatic monolith** — a single deployable Spring Boot application with internal boundaries enforced by package structure. The UI is server-side rendered with Thymeleaf, styled with Tailwind CSS, and built with Vite for asset processing.

This approach prioritizes simplicity and shipping speed for an MVP while maintaining clear boundaries that allow future extraction into modules or services.

## Core Stack

| Category         | Technology                           | Version  | Purpose                                  |
| ---------------- | ------------------------------------ | -------- | ---------------------------------------- |
| Language         | Java                                 | 21 (LTS) | Primary language                         |
| Framework        | Spring Boot                          | 3.5.x    | Application framework                    |
| Web              | Spring MVC                           | 6.x      | HTTP layer, controllers                  |
| Templates        | Thymeleaf + Layout Dialect           | 3.x      | Server-side HTML rendering               |
| CSS              | Tailwind CSS                         | 4.x      | Utility-first styling                    |
| Build (frontend) | Vite                                 | 6.x      | CSS processing, hot module replacement   |
| Database         | PostgreSQL                           | 17       | Primary relational datastore             |
| Migrations       | Flyway                               | 10.x     | Schema versioning and migrations         |
| ORM              | Spring Data JPA + Hibernate          | 6.x      | Persistence and query layer              |
| Mapping          | MapStruct                            | 1.6.x    | Compile-time DTO ↔ entity conversion     |
| Security         | Spring Security                      | 6.x      | Authentication, CSRF, session management |
| Validation       | Jakarta Validation (Bean Validation) | 3.x      | Input and domain validation              |

## Development Tools

| Tool                       | Purpose                                      |
| -------------------------- | -------------------------------------------- |
| Docker Compose             | Local PostgreSQL 17 + pgAdmin 4              |
| Spring Boot DevTools       | Live reload during development               |
| Maven                      | Build tool and dependency management         |
| frontend-maven-plugin      | Node/npm integration in Maven build          |
| Spring Java Format         | Code style enforcement (4-space indent)      |
| Prettier + Tailwind Plugin | HTML/CSS/JS formatting (2-space indent)      |
| EditorConfig               | Consistent line endings (LF) and indentation |

## Testing Stack

| Tool           | Purpose                                         |
| -------------- | ----------------------------------------------- |
| JUnit 5        | Unit and integration test framework             |
| Mockito        | Mocking for unit tests                          |
| Testcontainers | Real PostgreSQL instances for integration tests |
| JaCoCo         | Code coverage reporting (70% minimum threshold) |
| Maven Surefire | Runs unit tests (`*Tests.java`)                 |
| Maven Failsafe | Runs integration tests (`*IT.java`)             |

## Architecture Decisions

Key technology choices are documented as Architecture Decision Records:

| ADR                                                          | Decision                             |
| ------------------------------------------------------------ | ------------------------------------ |
| [ADR-0001](adr/0001-server-side-rendering-with-thymeleaf.md) | Server-side rendering with Thymeleaf |
| [ADR-0002](adr/0002-postgresql-as-primary-database.md)       | PostgreSQL as primary database       |
| [ADR-0003](adr/0003-mapstruct-for-dto-mapping.md)            | MapStruct for DTO mapping            |
| [ADR-0004](adr/0004-pragmatic-monolith-architecture.md)      | Pragmatic monolith architecture      |
| [ADR-0005](adr/0005-tailwind-css-with-vite.md)               | Tailwind CSS with Vite               |
| [ADR-0006](adr/0006-flyway-for-schema-migrations.md)         | Flyway for schema migrations         |
| [ADR-0007](adr/0007-testcontainers-for-integration-tests.md) | Testcontainers for integration tests |
