# ADR-0008: Render as Deployment Platform

## Status

Accepted

## Context

The MVP needs a hosting platform for the Spring Boot application and its PostgreSQL database. The main options are:

1. **Render** — PaaS with native Docker support, managed PostgreSQL, and infrastructure-as-code via Blueprints.
2. **Heroku** — established PaaS with buildpack-based deploys and managed Postgres.
3. **Railway** — developer-focused PaaS with automatic deploys and managed databases.
4. **Fly.io** — edge-first PaaS with Docker-based deploys and Fly Postgres.
5. **AWS (ECS/EKS)** — full cloud infrastructure with container orchestration.

Key requirements: free tier for MVP validation, managed PostgreSQL, Docker-based deploys, minimal operational overhead, and infrastructure-as-code for reproducibility.

## Decision

Use **Render** as the deployment platform with a **Blueprint** (`render.yaml`) for declarative infrastructure-as-code. The setup includes a Docker-based web service and a managed PostgreSQL 17 instance, both on the free tier in the Frankfurt region.

The Blueprint defines all services, databases, and environment variables declaratively. Render injects database connection properties (`host`, `port`, `database`, `user`, `password`) as environment variables into the web service, which Spring Boot resolves via placeholders in `application.properties`.

Continuous delivery is handled externally via a GitHub Actions workflow that triggers a Render deploy hook after CI passes on `main`.

## Consequences

### Positive

- **Infrastructure-as-code** — `render.yaml` defines the entire stack declaratively. New environments can be spun up from the Blueprint without manual configuration.
- **Free tier** — both the web service and PostgreSQL instance are available on the free plan, sufficient for MVP validation.
- **Managed PostgreSQL** — backups, maintenance, and connection management are handled by Render. No operational burden.
- **Docker-based deploys** — the existing `Dockerfile` is used directly. No proprietary buildpack or runtime required.
- **Environment variable injection** — database properties are injected from the managed database into the web service automatically via `fromDatabase` references in the Blueprint.

### Negative

- **Free tier cold starts** — the web service spins down after inactivity and takes several seconds to start on the first request.
- **Blueprint limitations** — environment variables in `render.yaml` do not support string interpolation or composition. The JDBC URL must be composed in `application.properties` using individual variables (`POSTGRES_HOST`, `POSTGRES_PORT`, `POSTGRES_DB`), not as a single `SPRING_DATASOURCE_URL`.
- **Vendor lock-in** — the Blueprint format is Render-specific and not portable to other platforms.

### Mitigation

- The `Dockerfile` follows standard multi-stage build conventions, making migration to any Docker-capable platform straightforward.
- Environment variables follow Spring Boot naming conventions (`SPRING_DATASOURCE_*`, `POSTGRES_*`), which are platform-agnostic.
- Cold starts are acceptable for an MVP. If response times become critical, upgrading to a paid plan eliminates the issue.
