# Implementation Order (Sprints)

| Sprint | Focus                      | Slices                             |
| ------ | -------------------------- | ---------------------------------- |
| 1      | Foundation + Core CRUD     | ID01-S1, ID01-S2, TR01-S1, TR01-S2 |
| 2      | Pipeline + Child Entities  | TR01-S3, TR02-S1, TR03-S1, TR03-S2 |
| 3      | Interviews, Offers, Search | TR04-S1, TR05-S1, TR06-S1, TR06-S2 |
| 4      | Dashboard                  | DA01-S1, DA01-S2, DA01-S3, TR06-S3 |
| 5      | Skills (P1)                | SK01-S1, SK01-S2, SK02-S1, SK02-S2 |
| 6      | Insights + Ghosting (P1)   | SK03-S1, DA02-S1, TR07-S1, TR07-S2 |

## Sprint Details

### Sprint 1 — Foundation + Core CRUD

- **ID01-S1:** User registration (Flyway V1, User aggregate, SecurityConfig)
- **ID01-S2:** Login and session management
- **TR01-S1:** Create a job application (Flyway V2, JobApplication aggregate)
- **TR01-S2:** View and edit applications

### Sprint 2 — Pipeline + Child Entities

- **TR01-S3:** Delete application with cascade confirmation
- **TR02-S1:** Pipeline state machine and status update (PipelineService)
- **TR03-S1:** Create and list follow-ups (Flyway V3, FollowUp entity)
- **TR03-S2:** Complete follow-up with outcome

### Sprint 3 — Interviews, Offers, Search

- **TR04-S1:** Create, list, update interviews (Flyway V4, Interview entity)
- **TR05-S1:** Create and manage offers (Flyway V5, Offer entity)
- **TR06-S1:** Free-text search (ILIKE on title/company)
- **TR06-S2:** Status filter (multi-select, Specification pattern)

### Sprint 4 — Dashboard

- **DA01-S1:** Application count metrics (DashboardQueryRepository, empty state)
- **DA01-S2:** Response rate, upcoming interviews, pending follow-ups
- **DA01-S3:** No-response alerts (14-day detection)
- **TR06-S3:** Date range, additional filters, sort options

### Sprint 5 — Skills (P1)

- **SK01-S1:** Skill catalog seed data (Flyway V6+V7, Skill aggregate)
- **SK01-S2:** Manual skill tagging on applications (Flyway V8)
- **SK02-S1:** Skill extraction engine (SkillExtractor, SkillNormalizer, domain event)
- **SK02-S2:** Extraction results display and manual override

### Sprint 6 — Insights + Ghosting (P1)

- **SK03-S1:** Skill frequency and top skills computation (TrendAnalyzer)
- **DA02-S1:** Insights page (ranked skills, category breakdown)
- **TR07-S1:** Ghosting detection (GhostingPolicy domain service)
- **TR07-S2:** Ghosting suggestion UI (dashboard alerts)
