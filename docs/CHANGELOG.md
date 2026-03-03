# Changelog

All notable changes to project documentation are documented in this file.

---

## 2026-03-03 — Infrastructure ADRs

Added four architecture decision records covering infrastructure and tooling choices:

- **ADR-0008: Render as Deployment Platform** — documents the choice of Render with Blueprint (`render.yaml`) for infrastructure-as-code, free-tier Docker-based deploys, and managed PostgreSQL
- **ADR-0009: GitHub Actions for CI/CD** — documents the two-workflow strategy: CI (`build.yml` with `./mvnw verify`) and CD (`deploy.yml` with Render deploy hook)
- **ADR-0010: Docker Compose for Local Development** — documents the three-service setup (PostgreSQL 17, pgAdmin 4, app) with Spring Boot Docker Compose support for auto-discovery
- **ADR-0011: Spring Java Format for Code Style** — documents the choice of Spring Java Format with build-time validation and one-command auto-fix

---

## 2026-03-03 — Build and startup fixes

Fixed three issues preventing `./mvnw verify` and `./mvnw spring-boot:run -Dspring-boot.run.profiles=local` from succeeding.

- Fixed Java formatting violations in `SecurityConfig.java` and `TestcontainersConfiguration.java` via `spring-javaformat:apply`
- Added `jacoco-maven-plugin` version (0.8.12) to `pom.xml` `<properties>`, eliminating the Maven warning about missing plugin version
- Added default values to environment placeholders in `application.properties` (`${POSTGRES_HOST:localhost}`, `${POSTGRES_PORT:5432}`, `${POSTGRES_DB:jobtracker_db}`, `${SPRING_DATASOURCE_USERNAME:postgres}`, `${SPRING_DATASOURCE_PASSWORD:postgres}`) so the app starts locally without requiring environment variables

---

## 2026-03-01 — Combined product and implementation roadmap

Added `docs/product/ROADMAP.md` — a single document that synthesizes MVP-SCOPE, GTM-STRATEGY, EPIC-MAP, JOURNEY, and METRICS-PLAN into a unified view.

- Vertical slice plan (VS-00 through VS-11) with slice assignments, Flyway versions, and deliverables per week
- Dependency map showing blocking relationships between all 23 slices
- Launch phases (Private Beta → Open Beta → Public Launch) with entry gates and success criteria
- Release criteria checklist (functional, technical, operational)
- Metrics by phase (product metrics, acquisition metrics, hypothesis validation schedule)
- Future backlog (P2 features)

---

## 2026-03-01 — Cross-document consistency fixes

Reviewed product docs, strategic DDD, and tactical DDD for consistency. Fixed 7 inconsistencies (5 major, 2 moderate).

### Major fixes

1. **Domain Events gap** (`02-tactical-ddd/DOMAIN-EVENTS.md`)
   - Added 6 missing events: `UserRegistered`, `JobApplicationCreated`, `FollowUpScheduled`, `FollowUpCompleted`, `InterviewScheduled`, `OfferReceived`
   - Previously only `JobDescriptionUpdated` and `ApplicationStatusChanged` were defined; product docs (`metrics/instrumentation-approach.md`) expected 8
   - Changed `ApplicationStatusChanged` subscriber from "Dashboard (future)" to "Dashboard (metrics)"

2. **SkillCategory missing SOFT_SKILL** (`01-strategic-ddd/subdomains.md`)
   - Added "Soft Skill" to the skill category taxonomy (was listing 9 categories; product docs and tactical DDD both list 10)

3. **JobSkill write path undefined** (`02-tactical-ddd/AGGREGATES.md`, `02-tactical-ddd/CROSS-CONTEXT.md`)
   - Added `JobSkill` as a cross-context association entity in `AGGREGATES.md` under Skills context, with fields, ownership, creation triggers, and repository
   - Added two rows to `CROSS-CONTEXT.md` Communication Map: `JobApplicationCreated` event (Tracking → Skills) and JobSkill write ownership (Skills → Tracking table)

4. **Tracking→Skills relationship type** (`01-strategic-ddd/context-map.md`)
   - Changed from "Conformist" to "Customer-Supplier" — Skills does not conform to Tracking's model; it receives primitive data via domain events and processes it with its own logic
   - Updated diagram label (CF → CS), legend, section title, and description

5. **OFFER listed as terminal state** (`01-strategic-ddd/ubiquitous-language.md`)
   - Corrected Domain Rule #4: OFFER is not terminal — it can transition to Rejected or Withdrawn (aligned with PipelineService in tactical DDD and RF1 in product docs)

### Moderate fixes

6. **ApplicationStatusChanged subscriber** (`02-tactical-ddd/DOMAIN-EVENTS.md`)
   - Addressed as part of fix #1 — changed "(future)" to "(metrics)"

7. **Recruiter modeling decision** (`02-tactical-ddd/AGGREGATES.md`)
   - Added design note explaining that recruiter data is stored as flat fields (recruiterName, recruiterCompany, recruiterEmail) rather than a separate value object
