# ADR-0010: Docker Compose for Local Development

## Status

Accepted

## Context

Developers need a local environment that mirrors production as closely as possible. The application depends on PostgreSQL 17 and benefits from a database administration tool during development. The main options are:

1. **Docker Compose** — multi-container orchestration with declarative service definitions.
2. **Local PostgreSQL installation** — install PostgreSQL directly on the developer's machine.
3. **Dev Containers** — full development environment inside a container, including IDE tooling.
4. **Testcontainers for development** — programmatic container management from the application.

## Decision

Use **Docker Compose** (`compose.yaml`) to orchestrate three services for local development:

1. **PostgreSQL 17** (`db`) — primary database with health checks, volume persistence, and an init script (`init-schemas.sql`) for bootstrapping the `core` schema.
2. **pgAdmin 4** (`pgadmin`) — web-based database administration tool, pre-configured to connect to the local database.
3. **Application** (`app`) — the Spring Boot application running inside a container, used for Docker-based testing (not for daily development, where `./mvnw spring-boot:run` is preferred).

**Spring Boot Docker Compose support** auto-detects the running containers and contributes connection properties when the application runs locally via `./mvnw spring-boot:run -Dspring-boot.run.profiles=local`. Configuration is externalized via a `.env` file with defaults in `compose.yaml` for zero-config startup.

## Consequences

### Positive

- **One-command setup** — `docker compose up` starts the entire stack. No manual database installation or configuration required.
- **Environment parity** — developers run the same PostgreSQL 17 version as production (Render managed PostgreSQL).
- **pgAdmin included** — database inspection, query execution, and schema exploration are available out of the box at `localhost:5050`.
- **Spring Boot auto-discovery** — the Docker Compose support module detects running services and injects connection properties automatically, eliminating manual datasource configuration for local development.
- **Configurable via `.env`** — ports, credentials, and database names can be customized per developer without modifying `compose.yaml`.
- **Volume persistence** — database data survives container restarts via named volumes (`jobtracker_postgres_data`).

### Negative

- **Docker Desktop required** — developers must have Docker Desktop (or a compatible runtime) installed, which adds disk space and memory overhead.
- **Memory overhead** — three containers consume resources even when idle. Default configuration reserves 512 MB per container.
- **Network complexity** — services communicate via a Docker bridge network (`jobtracker-network`). The database hostname inside Docker (`db`) differs from the host perspective (`localhost`), which can cause confusion.

### Mitigation

- `mem_limit` and `mem_reservation` are set on all containers to cap resource usage.
- Docker Desktop is an industry-standard tool that most developers already have installed.
- The `.env` file allows port customization to avoid conflicts with other local services.
