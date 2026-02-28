# Bounded Contexts

## Design Philosophy

This is an MVP. We follow the **pragmatic monolith** approach: all bounded contexts live in a single deployable Spring Boot application, separated by Java packages under `dev.ivoencarnacao.jobtracker`. Boundaries are enforced by package structure and discipline, not by separate services or databases.

The schema is single (`core` in PostgreSQL), and all contexts share the same transaction boundary. This is intentional for MVP simplicity. The package boundaries established now will make future extraction into modules or services straightforward.

---

## 1. Identity Context

**Package:** `dev.ivoencarnacao.jobtracker.identity`

**Owns subdomain:** Identity and Access Management (Supporting)

**Responsibility:** Manages user registration, authentication, and profile. Provides the `userId` that all other contexts use to scope and isolate data per user.

**Aggregates:**

- **User** (aggregate root) -- registration data, credentials, profile

**Key services:**

- RegistrationService -- handles new user creation
- AuthenticationService -- login/session management (delegates to Spring Security)

**Exposed data:** User identity (userId) consumed by all other contexts for data ownership.

---

## 2. Tracking Context

**Package:** `dev.ivoencarnacao.jobtracker.tracking`

**Owns subdomain:** Application Tracking (Core)

**Responsibility:** The central context. Manages the full lifecycle of job applications including their pipeline state, interviews, follow-ups, and offers. All data is scoped by userId.

**Aggregates:**

- **JobApplication** (aggregate root) -- contains or references:
  - ApplicationStatus (value object / enum)
  - Interview (entity, child of aggregate)
  - FollowUp (entity, child of aggregate)
  - Offer (entity, child of aggregate)

**Key domain services:**

- PipelineService -- enforces valid state transitions
- GhostingPolicy -- implements the 21-day auto-suggestion rule

**Exposed data:** Job application details, status, associated interviews, follow-ups, offers, and the job posting description (consumed by Skills context).

---

## 3. Skills Context

**Package:** `dev.ivoencarnacao.jobtracker.skills`

**Owns subdomain:** Skills and Trends Analysis (Core)

**Responsibility:** Manages the skill catalog, extracts skills from job descriptions, normalizes skill names, categorizes skills, and computes frequency/trend analytics. Data is scoped by userId.

**Aggregates:**

- **Skill** (aggregate root) -- canonical skill definition with:
  - name (canonical)
  - aliases (value object collection)
  - category (SkillCategory enum)

**Key domain services:**

- SkillExtractor -- parses job description text to find skill mentions
- SkillNormalizer -- maps raw text matches to canonical Skill entities
- TrendAnalyzer -- computes frequencies, top-N, temporal trends

**Depends on:** Tracking Context (needs job application data and descriptions to extract and associate skills).

---

## 4. Dashboard Context

**Package:** `dev.ivoencarnacao.jobtracker.dashboard`

**Owns subdomain:** Reporting and Dashboard (Supporting)

**Responsibility:** A read-only context that queries data from Tracking and Skills to produce aggregate metrics and insights for the UI. Contains no aggregates of its own -- only query services and read models (DTOs/view models). Data is scoped by userId.

**Key services:**

- DashboardService -- computes metrics (counts by status, response rate, pending follow-ups, upcoming interviews, weekly activity)
- InsightsService -- presents skill trends (top skills, category breakdown, temporal analysis)

**Depends on:** Tracking Context (application data), Skills Context (skill frequency data).

---

## Package Structure (Clean Architecture)

Each bounded context follows Clean Architecture with four layers:

```
dev.ivoencarnacao.jobtracker/
  identity/
    domain/          # User aggregate, value objects, repository interfaces
    application/     # Use cases, input/output DTOs, service interfaces
    infrastructure/  # JPA repository implementations, Spring Security config, mappers
    web/             # Controllers, request/response DTOs, view models
  tracking/
    domain/          # JobApplication aggregate (with Interview, FollowUp, Offer),
                     #   ApplicationStatus value object, domain services,
                     #   repository interfaces
    application/     # Use cases (CreateJobApplication, UpdatePipelineStatus,
                     #   ScheduleFollowUp...), input/output DTOs, service interfaces
    infrastructure/  # JPA repository implementations, mappers (MapStruct)
    web/             # Controllers, request/response DTOs
  skills/
    domain/          # Skill aggregate, SkillCategory value object,
                     #   domain services (SkillExtractor, SkillNormalizer),
                     #   repository interfaces
    application/     # Use cases (ExtractSkills, AnalyzeTrends...),
                     #   input/output DTOs
    infrastructure/  # JPA repository implementations, skill dictionary data,
                     #   mappers
    web/             # Controllers, request/response DTOs
  dashboard/
    application/     # Query use cases (GetDashboardMetrics, GetSkillInsights),
                     #   read-model DTOs
    infrastructure/  # Read-only query implementations (JPA projections)
    web/             # Controllers, view models
  shared/
    config/          # Cross-cutting: web config, global exception handling
```

**Dependency rule:** `domain --> (nothing) | application --> domain | infrastructure --> application, domain | web --> application`
