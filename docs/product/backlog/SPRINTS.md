# Delivery Plan (Vertical Slices)

Continuous flow — one vertical slice per week, deployed to staging and production.

| VS | Name | Backlog Slices | Week | Priority |
| --- | --- | --- | --- | --- |
| VS-00 | Infrastructure & CI/CD | — | W1 | P0 |
| VS-01 | User Authentication | ID01-S1, ID01-S2 | W2 | P0 |
| VS-02 | Application CRUD | TR01-S1, TR01-S2 | W3 | P0 |
| VS-03 | Pipeline + Delete | TR02-S1, TR01-S3 | W4 | P0 |
| VS-04 | Follow-up Reminders | TR03-S1, TR03-S2 | W5 | P0 |
| VS-05 | Interviews + Offers | TR04-S1, TR05-S1 | W6 | P0 |
| VS-06 | Search & Filter | TR06-S1, TR06-S2 | W7 | P0 |
| VS-07 | Dashboard | DA01-S1, DA01-S2, DA01-S3 | W8 | P0 |
| — | Beta Launch + Stabilization | — | W9 | — |
| VS-08 | Ghosting Detection | TR07-S1, TR07-S2 | W10 | P1 |
| VS-09 | Skill Catalog + Tagging | SK01-S1, SK01-S2 | W11 | P1 |
| VS-10 | Skill Extraction | SK02-S1, SK02-S2 | W12 | P1 |
| VS-11 | Insights + Advanced Filters | SK03-S1, DA02-S1, TR06-S3 | W13 | P1 |

---

## VS-00 — Infrastructure & CI/CD (Week 1)

- GitHub Actions CI pipeline (build, test, format-check on push/PR)
- Staging deployment (Docker-based)
- Production deployment with environment config
- Flyway baseline: `V1__create_schema.sql` (CREATE SCHEMA core)
- Testcontainers setup with abstract IT base class
- Landing page at `/` with value proposition and CTA
- Spring Actuator health check endpoint

**Deploy:** Landing page live on staging + production. CI pipeline green.

## VS-01 — User Authentication (Week 2)

- **Domain:** User aggregate, repository interface
- **Application:** RegisterUserUseCase, DTOs
- **Infrastructure:** JPA UserEntity, MapStruct mapper, SecurityConfig, BCrypt
- **Web:** Registration form (`/register`), login form (`/login`), controllers
- **Migration:** `V2__create_users_table.sql`
- **Tests:** Unit tests for domain, integration tests for registration + login flow

**Deploy:** Users can register and log in. First functional production deploy.

## VS-02 — Application CRUD (Week 3)

- **Domain:** JobApplication aggregate, value objects (ApplicationStatus enum type), repository interface
- **Application:** CreateApplicationUseCase, UpdateApplicationUseCase, ListApplicationsUseCase, DTOs
- **Infrastructure:** JPA entity, MapStruct mapper, JPA repository implementation
- **Web:** Create form (`/applications/new`), list page (`/applications`), detail page (`/applications/{id}`), edit form (`/applications/{id}/edit`), controllers
- **Migration:** `V3__create_job_applications_table.sql` with PostgreSQL enum types
- **Tests:** Unit + integration tests

**Deploy:** Authenticated users can create, list, view, and edit job applications.

## VS-03 — Pipeline + Delete (Week 4)

- **Domain:** PipelineService domain service (11 states, transition validation), ApplicationStatusChanged event
- **Application:** UpdateStatusUseCase with validation rules
- **Web:** Status dropdown on detail page with valid transitions only, `PATCH /applications/{id}/status`
- **Delete:** Cascade confirmation dialog, `DELETE /applications/{id}`
- **Tests:** State machine unit tests (all valid/invalid transitions), delete integration tests

**Deploy:** Users can advance applications through pipeline states and delete applications.

## VS-04 — Follow-up Reminders (Week 5)

- **Domain:** FollowUp entity (child of JobApplication aggregate), FollowUpOutcome, FollowUpScheduled/FollowUpCompleted events
- **Application:** CreateFollowUpUseCase, CompleteFollowUpUseCase, DTOs
- **Infrastructure:** JPA entity, Flyway migration
- **Web:** Inline section on application detail page — add form, list (ordered by due date, overdue highlighted), complete action
- **Migration:** `V4__create_follow_ups_table.sql`
- **Tests:** Unit + integration tests

**Deploy:** Users can set follow-up reminders and mark them complete with outcome.

## VS-05 — Interviews + Offers (Week 6)

- **Domain:** Interview entity (child of JobApplication), InterviewType, InterviewOutcome; Offer entity (1-to-1 with application), OfferStatus; InterviewScheduled and OfferReceived events
- **Application:** Use cases for both entities, DTOs
- **Infrastructure:** JPA entities, Flyway migrations
- **Web:** Inline sections on application detail — interview add/edit form, offer create/edit form
- **Migrations:** `V5__create_interviews_table.sql`, `V6__create_offers_table.sql`
- **Tests:** Unit + integration tests

**Deploy:** Users can record interviews (type, date, outcome) and offers (salary, status) on applications.

## VS-06 — Search & Filter (Week 7)

- **Application:** SearchApplicationsUseCase with combined criteria
- **Infrastructure:** ILIKE query on company/title, Specification pattern for status multi-select filter, pagination
- **Web:** Search bar + status filter dropdowns on application list page
- **Tests:** Search and filter integration tests with varied data sets

**Deploy:** Users can search applications by text and filter by status.

## VS-07 — Dashboard (Week 8)

- **Domain:** Query service interfaces (read-only, cross-aggregate)
- **Application:** DashboardQueryUseCase, metric DTOs
- **Infrastructure:** DashboardQueryRepository with native queries — total count, count by status, this week, response rate, upcoming interviews, pending follow-ups, 14-day no-response alerts
- **Web:** Dashboard page (`/dashboard`) with metric cards, alerts, empty state handling
- **Tests:** Dashboard query integration tests with realistic test data

**Deploy:** Complete MVP dashboard. **Private Beta Gate.**

## Beta Launch + Stabilization (Week 9)

- Invite 10-20 testers, prepare feedback channels
- Analytics integration (Plausible/Umami)
- In-app feedback widget
- Bug fixes from self-testing
- Error logging and basic monitoring

## VS-08 — Ghosting Detection (Week 10)

- **Domain:** GhostingPolicy domain service (21-day rule on APPLIED/UNDER_REVIEW), isGhostingCandidate()
- **Application:** DetectGhostingCandidatesUseCase, suggestion DTOs
- **Web:** Dashboard alert section — "Mark as ghosted?" suggestions with accept/dismiss actions
- **Tests:** GhostingPolicy unit tests, integration tests
- Continued beta stabilization

**Deploy:** Users see ghosting suggestions for stale applications.

## VS-09 — Skill Catalog + Tagging (Week 11)

- **Domain:** Skill aggregate, SkillCategory enum, SkillAlias value objects
- **Infrastructure:** Flyway seed migration with ~100 skills and aliases
- **Cross-context:** JobApplicationCreated event triggers skill association setup; manual tagging via Spring events
- **Web:** Tag dropdown + chips on application detail, remove tag action
- **Migrations:** `V7__create_skills_tables.sql`, `V8__create_job_application_skills_table.sql`
- **Tests:** Catalog queries, tagging integration tests

**Deploy:** Users can manually tag skills on applications from a curated catalog.

## VS-10 — Skill Extraction (Week 12)

- **Domain:** SkillExtractor domain service (case-insensitive keyword matching, word-boundary aware), SkillNormalizer (alias → canonical Skill resolution)
- **Application:** ExtractSkillsUseCase triggered by JobDescriptionUpdated event
- **Web:** Extracted skills display on application detail, accept/reject actions
- **Tests:** Extraction accuracy tests with sample job descriptions from `docs/references/`

**Deploy:** System auto-extracts skills from job descriptions; users can accept or reject.

## VS-11 — Insights + Advanced Filters (Week 13)

- **Domain:** TrendAnalyzer (frequency computation, top-N, category breakdown)
- **Application:** SkillInsightsQueryUseCase, DTOs
- **Infrastructure:** SkillInsightsQueryRepository
- **Web:** Insights page (`/insights`) with top skills ranked, category breakdown
- **Advanced filters:** Date range, sort options (date, last updated, company A-Z) on application list
- **Tests:** Insights queries, filter integration tests

**Deploy:** Skill insights page live + advanced filtering. **Open Beta Ready.**
