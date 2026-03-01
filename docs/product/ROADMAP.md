# Roadmap

## Overview

```
MVP Development                         Launch Phases
========================               ====================================

Sprint 1 ─ Foundation + CRUD    (P0)
Sprint 2 ─ Pipeline + Follow-ups (P0)
Sprint 3 ─ Interviews + Search   (P0)
Sprint 4 ─ Dashboard             (P0)
         ↓
         ├─── Phase 1: Private Beta (2-3 weeks, 10-20 testers)
         │
Sprint 5 ─ Skills Foundation     (P1)
Sprint 6 ─ Insights + Ghosting   (P1)
         │
         ├─── Phase 2: Open Beta (4-6 weeks, organic growth)
         │
         └─── Phase 3: Public Launch (Product Hunt, communities)
```

**Scope:** 12 epics, 23 slices, 4 bounded contexts (Identity, Tracking, Skills, Dashboard)

---

## Sprint Plan

### Sprint 1 — Foundation + Core CRUD (P0)

| Slice | Description | Context | Notes |
|-------|-------------|---------|-------|
| ID01-S1 | User Registration | Identity | Flyway V1 (users table) |
| ID01-S2 | Login & Session Management | Identity | Spring Security, BCrypt |
| TR01-S1 | Create Job Application | Tracking | Flyway V2 (job_applications table) |
| TR01-S2 | View & Edit Application | Tracking | |

**Deliverable:** Users can register, log in, create and edit job applications.

### Sprint 2 — Pipeline + Follow-ups (P0)

| Slice | Description | Context | Notes |
|-------|-------------|---------|-------|
| TR01-S3 | Delete Application | Tracking | Cascade confirmation |
| TR02-S1 | Pipeline State Machine | Tracking | PipelineService, 11 states |
| TR03-S1 | Create & List Follow-ups | Tracking | Flyway V3 (follow_ups table) |
| TR03-S2 | Complete Follow-up | Tracking | Outcome: Responded / No Response |

**Deliverable:** Full pipeline tracking and follow-up management.

### Sprint 3 — Interviews, Offers, Search (P0)

| Slice | Description | Context | Notes |
|-------|-------------|---------|-------|
| TR04-S1 | Interview Management | Tracking | Flyway V4 (interviews table) |
| TR05-S1 | Offer Recording | Tracking | Flyway V5 (offers table) |
| TR06-S1 | Free-text Search | Tracking | ILIKE on title/company |
| TR06-S2 | Status Filter | Tracking | Multi-select filter |

**Deliverable:** Interview/offer tracking and basic search/filter.

### Sprint 4 — Dashboard (P0)

| Slice | Description | Context | Notes |
|-------|-------------|---------|-------|
| DA01-S1 | Application Count Metrics | Dashboard | Total, by status, this week |
| DA01-S2 | Response Rate & Activity | Dashboard | Response rate, upcoming interviews, pending follow-ups |
| DA01-S3 | No-response Alerts | Dashboard | 14-day warning threshold |

**Deliverable:** Complete MVP dashboard with core metrics.

> **MILESTONE: Private Beta Ready** — All P0 slices complete. See [Release Criteria](#release-criteria).

### Sprint 5 — Skills Foundation (P1)

| Slice | Description | Context | Notes |
|-------|-------------|---------|-------|
| SK01-S1 | Seed Skill Catalog | Skills | Flyway V6-V7 (skills, aliases tables), ~100 skills |
| SK01-S2 | Manual Skill Tagging | Skills | Flyway V8 (job_application_skills table) |
| SK02-S1 | Extraction Engine | Skills | SkillExtractor, SkillNormalizer |
| SK02-S2 | Extraction Results Display | Skills | UI for extracted skills, manual override |

**Deliverable:** Skill tagging (manual + automatic extraction).

### Sprint 6 — Insights + Ghosting + Advanced Filters (P1)

| Slice | Description | Context | Notes |
|-------|-------------|---------|-------|
| SK03-S1 | Skill Frequency | Skills | TrendAnalyzer |
| DA02-S1 | Insights Page | Dashboard | Top skills, category breakdown |
| TR07-S1 | Ghosting Detection | Tracking | GhostingPolicy (21-day rule) |
| TR07-S2 | Ghosting Suggestion UI | Tracking | Dashboard alert |
| TR06-S3 | Advanced Filters & Sort | Tracking | Date range, work mode, sort options |

**Deliverable:** Skill insights, auto-ghosting suggestions, advanced filters.

---

## Dependency Map

```
ID01-S1 (Registration)
  └─ ID01-S2 (Login)
       └─ TR01-S1 (Create Application)
            ├─ TR01-S2 (View/Edit)
            ├─ TR01-S3 (Delete)
            ├─ TR02-S1 (Pipeline)
            │    └─ TR07-S1 (Ghosting Detection)
            │         └─ TR07-S2 (Ghosting UI)
            ├─ TR03-S1 (Follow-ups)
            │    └─ TR03-S2 (Complete Follow-up)
            ├─ TR04-S1 (Interviews)
            ├─ TR05-S1 (Offers)
            ├─ TR06-S1 (Search)
            │    └─ TR06-S2 (Status Filter)
            │         └─ TR06-S3 (Advanced Filters)
            └─ DA01-S1 (Count Metrics)
                 └─ DA01-S2 (Response Rate)
                      └─ DA01-S3 (Alerts)

SK01-S1 (Seed Catalog)
  └─ SK01-S2 (Manual Tagging) ← requires TR01-S1
       └─ SK02-S1 (Extraction Engine) ← requires JobDescriptionUpdated event
            └─ SK02-S2 (Extraction Display)
                 └─ SK03-S1 (Skill Frequency)
                      └─ DA02-S1 (Insights Page)
```

**Critical path:** ID01-S1 → TR01-S1 → DA01-S1 (blocks entire MVP)

---

## Launch Phases

### Phase 1 — Private Beta

| | |
|---|---|
| **Duration** | 2-3 weeks |
| **Audience** | 10-20 trusted testers (university peers, developer contacts, career changers) |
| **Goal** | Validate core flows, identify friction, collect qualitative feedback |
| **Entry gate** | All P0 slices complete (Sprints 1-4). See [Release Criteria](#release-criteria). |
| **Feedback** | Direct messaging + in-app feedback form |
| **Success signal** | Core flows work end-to-end without critical bugs |

### Phase 2 — Open Beta

| | |
|---|---|
| **Duration** | 4-6 weeks |
| **Audience** | Open registration with landing page |
| **Goal** | Validate product-market fit with organic traffic |
| **Entry gate** | Phase 1 feedback addressed, P1 features shipping (Sprints 5-6) |
| **Metrics** | Activation rate > 30%, week-1 retention > 40% |
| **Success signal** | Sustained organic user growth |

### Phase 3 — Public Launch

| | |
|---|---|
| **Audience** | Product Hunt, Reddit, LinkedIn, dev communities |
| **Goal** | 100+ registered users in first month |
| **Entry gate** | Phase 2 metrics validated, all features stable |

---

## Release Criteria

### Private Beta Gate (after Sprint 4)

**Functional:**
- All P0 slices implemented and tested
- All acceptance criteria for P0 slices verified
- Dashboard displays correct metrics with real data
- End-to-end flow: register → login → create application → update status → view dashboard

**Technical:**
- JaCoCo >= 70% line coverage
- Zero critical vulnerabilities (CSRF, XSS, SQL injection, IDOR)
- Performance: dashboard < 2s, form submission < 500ms
- Flyway migrations V1-V5 execute without errors

**Operational:**
- Functional deploy on staging environment
- Database backup configured
- In-app feedback widget integrated
- Analytics (Plausible/Umami) configured and capturing events

---

## Metrics by Phase

### Product Metrics (all phases)

| Metric | Target | When |
|--------|--------|------|
| Applications per user (week 1) | 10+ | Phase 1 |
| Weekly active users | 2+ sessions/week | Phase 1+ |
| Status update rate | > 50% | Phase 1+ |
| Follow-up usage | > 30% of users | Phase 1+ |
| Insights page visits | > 25% of users | Phase 2 (after P1 ships) |
| Known final state | > 60% of applications | Phase 2+ |
| Time to first value | < 3 minutes | Phase 1+ |

### Acquisition Metrics (Phase 2+)

| Metric | Target |
|--------|--------|
| Conversion rate (registrations / landing visits) | > 5% |
| Activation rate (5+ apps / registrations) | > 30% |
| Week-1 retention (2+ sessions in week 2) | > 40% |
| Referral rate | Track (no target yet) |

### Hypothesis Validation Schedule

| # | Hypothesis | Validated in |
|---|-----------|-------------|
| H1 | Users manually input if form < 1 minute | Phase 1 (Sprint 1) |
| H2 | Job descriptions yield 3+ recognized skills | Phase 2 (Sprint 5) |
| H3 | Tracking + insights > tracking alone (higher WAU) | Phase 2 (Sprint 6) |
| H4 | Follow-ups reduce ghosting rate | Phase 1 (Sprint 2) |

---

## Future Backlog (P2)

Features for iteration after Open Beta, based on validated demand:

| Feature | Description |
|---------|-------------|
| Temporal skill trends | Frequency changes over last 30/60 days |
| Recruiter contact management | Structured recruiter details per application |
| Salary comparison | Side-by-side offer comparison across applications |
| CSV export | Export application data for external use |
| Filter by source/work mode | Additional filter dimensions |
| Kanban board view | Visual pipeline management |

---

## References

- [MVP Scope](MVP-SCOPE.md) — Priority classification (P0/P1/P2)
- [GTM Strategy](GTM-STRATEGY.md) — Launch phases and promotion channels
- [Epic Map](backlog/EPIC-MAP.md) — All epics and slices
- [Global User Journey](backlog/JOURNEY.md) — End-to-end user flow
- [Metrics Plan](METRICS-PLAN.md) — Precise metric definitions and instrumentation
