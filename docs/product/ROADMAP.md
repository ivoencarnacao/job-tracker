# Roadmap — Q2 2026

## Overview

```
Q2 2026 (April 1 – June 30)                                 Launch Phases
Continuous flow · Vertical slices · Deploy every week        ═══════════════════════════

W1  VS-00  Infrastructure & CI/CD ──────┐
W2  VS-01  User Authentication          │
W3  VS-02  Application CRUD             │  P0 (MVP)
W4  VS-03  Pipeline + Delete            │
W5  VS-04  Follow-up Reminders          │
W6  VS-05  Interviews + Offers          │
W7  VS-06  Search & Filter              │
W8  VS-07  Dashboard                 ───┘
                ↓
                ├─── Phase 1: Private Beta (W9, 10-20 testers)
                │
W10 VS-08  Ghosting Detection           │
W11 VS-09  Skill Catalog + Tagging      │  P1
W12 VS-10  Skill Extraction             │
W13 VS-11  Insights + Advanced Filters  │
                │
                ├─── Phase 2: Open Beta (organic growth)
                │
                └─── Phase 3: Public Launch (Product Hunt, communities)
```

**Scope:** 13 epics, 23 backlog slices, 4 bounded contexts (Identity, Tracking, Skills, Dashboard)
**Delivery model:** Continuous flow — one vertical slice per week, deployed to staging and production

---

## Vertical Slice Plan

Each vertical slice is independently deployable and delivers user-facing value. Backlog slices are grouped when shipping them separately would not provide a useful increment.

| Week | Vertical Slice                     | Backlog Slices              | Deliverable                                              |
| ---- | ---------------------------------- | --------------------------- | -------------------------------------------------------- |
| W1   | VS-00: Infrastructure & CI/CD      | —                           | Landing page live, CI green, staging + production deploy |
| W2   | VS-01: User Authentication         | ID01-S1 + ID01-S2           | Register + login functional in production                |
| W3   | VS-02: Application CRUD            | TR01-S1 + TR01-S2           | Create, list, view, edit applications                    |
| W4   | VS-03: Pipeline + Delete           | TR02-S1 + TR01-S3           | 11-state pipeline management + delete                    |
| W5   | VS-04: Follow-up Reminders         | TR03-S1 + TR03-S2           | Create and complete follow-ups                           |
| W6   | VS-05: Interviews + Offers         | TR04-S1 + TR05-S1           | Interview and offer tracking                             |
| W7   | VS-06: Search & Filter             | TR06-S1 + TR06-S2           | Free-text search + status filter                         |
| W8   | VS-07: Dashboard                   | DA01-S1 + DA01-S2 + DA01-S3 | Core metrics, response rate, alerts                      |
| W9   | Beta Launch + Stabilization        | —                           | Analytics, feedback widget, bug fixes                    |
| W10  | VS-08: Ghosting Detection          | TR07-S1 + TR07-S2           | Ghosting suggestions on dashboard                        |
| W11  | VS-09: Skill Catalog + Tagging     | SK01-S1 + SK01-S2           | Manual skill tagging from curated catalog                |
| W12  | VS-10: Skill Extraction            | SK02-S1 + SK02-S2           | Auto-extraction from job descriptions                    |
| W13  | VS-11: Insights + Advanced Filters | SK03-S1 + DA02-S1 + TR06-S3 | Skill insights page + advanced filters                   |

### Merge Rationale

| Merge                               | Reason                                                             |
| ----------------------------------- | ------------------------------------------------------------------ |
| ID01-S1 + ID01-S2 → VS-01           | Registration without login has no user value                       |
| TR01-S1 + TR01-S2 → VS-02           | Creating an application without viewing it is not useful           |
| TR03-S1 + TR03-S2 → VS-04           | Follow-up creation and completion are a single user story          |
| DA01-S1 + DA01-S2 + DA01-S3 → VS-07 | All three are read-only queries on the same dashboard page         |
| TR07-S1 + TR07-S2 → VS-08           | Ghosting detection without the UI delivers zero value              |
| TR06-S1 + TR06-S2 → VS-06           | Search and status filter form a single "find applications" feature |

---

## Deployment Milestones

| Date         | Milestone               | Gate Criteria                                                          |
| ------------ | ----------------------- | ---------------------------------------------------------------------- |
| Apr 4 (W1)   | Infrastructure Live     | CI green, staging + production deploy working, landing page accessible |
| Apr 11 (W2)  | First Functional Deploy | Registration + login working in production                             |
| Apr 18 (W3)  | Core Tracking           | Users can create and manage applications in production                 |
| May 23 (W8)  | **Private Beta Gate**   | All P0 slices complete, [release criteria](#release-criteria) met      |
| Jun 6 (W10)  | P1 Phase 1              | Ghosting detection live, beta stabilized                               |
| Jun 27 (W13) | **Open Beta Ready**     | All P1 slices complete, insights page live                             |

---

## Dependency Map

```
ID01-S1 (Registration)
  └─ ID01-S2 (Login)                                        ── VS-01
       └─ TR01-S1 (Create Application)
            ├─ TR01-S2 (View/Edit)                          ── VS-02
            ├─ TR01-S3 (Delete)                             ┐
            ├─ TR02-S1 (Pipeline)                           ┘  VS-03
            │    └─ TR07-S1 (Ghosting Detection)            ┐
            │         └─ TR07-S2 (Ghosting UI)              ┘  VS-08
            ├─ TR03-S1 (Follow-ups)                         ┐
            │    └─ TR03-S2 (Complete Follow-up)            ┘  VS-04
            ├─ TR04-S1 (Interviews)                         ┐
            ├─ TR05-S1 (Offers)                             ┘  VS-05
            ├─ TR06-S1 (Search)                             ┐
            │    └─ TR06-S2 (Status Filter)                 ┘  VS-06
            │         └─ TR06-S3 (Advanced Filters)         ── VS-11
            └─ DA01-S1 (Count Metrics)                      ┐
                 └─ DA01-S2 (Response Rate)                 │  VS-07
                      └─ DA01-S3 (Alerts)                   ┘

SK01-S1 (Seed Catalog)                                      ┐
  └─ SK01-S2 (Manual Tagging) ← requires TR01-S1           ┘  VS-09
       └─ SK02-S1 (Extraction Engine)                       ┐
            └─ SK02-S2 (Extraction Display)                 ┘  VS-10
                 └─ SK03-S1 (Skill Frequency)               ┐
                      └─ DA02-S1 (Insights Page)            ┘  VS-11
```

**Critical path:** ID01-S1 → TR01-S1 → DA01-S1 (blocks entire MVP)

---

## Launch Phases

### Phase 1 — Private Beta

|                    |                                                                                          |
| ------------------ | ---------------------------------------------------------------------------------------- |
| **Duration**       | 2-3 weeks (starting W9)                                                                  |
| **Audience**       | 10-20 trusted testers (university peers, developer contacts, career changers)            |
| **Goal**           | Validate core flows, identify friction, collect qualitative feedback                     |
| **Entry gate**     | All P0 slices complete (VS-01 through VS-07). See [Release Criteria](#release-criteria). |
| **Feedback**       | Direct messaging + in-app feedback form                                                  |
| **Success signal** | Core flows work end-to-end without critical bugs                                         |

### Phase 2 — Open Beta

|                    |                                                                        |
| ------------------ | ---------------------------------------------------------------------- |
| **Duration**       | 4-6 weeks                                                              |
| **Audience**       | Open registration with landing page                                    |
| **Goal**           | Validate product-market fit with organic traffic                       |
| **Entry gate**     | Phase 1 feedback addressed, P1 features shipping (VS-08 through VS-11) |
| **Metrics**        | Activation rate > 30%, week-1 retention > 40%                          |
| **Success signal** | Sustained organic user growth                                          |

### Phase 3 — Public Launch

|                |                                                 |
| -------------- | ----------------------------------------------- |
| **Audience**   | Product Hunt, Reddit, LinkedIn, dev communities |
| **Goal**       | 100+ registered users in first month            |
| **Entry gate** | Phase 2 metrics validated, all features stable  |

---

## Release Criteria

### Private Beta Gate (after VS-07, Week 8)

**Functional:**

- All P0 slices implemented and tested
- All acceptance criteria for P0 slices verified
- Dashboard displays correct metrics with real data
- End-to-end flow: register → login → create application → update status → view dashboard

**Technical:**

- JaCoCo >= 70% line coverage
- Zero critical vulnerabilities (CSRF, XSS, SQL injection, IDOR)
- Performance: dashboard < 2s, form submission < 500ms
- Flyway migrations execute without errors

**Operational:**

- Functional deploy on staging and production
- Database backup configured
- In-app feedback widget integrated
- Analytics (Plausible/Umami) configured and capturing events

---

## Metrics by Phase

### Product Metrics (all phases)

| Metric                         | Target                | When                     |
| ------------------------------ | --------------------- | ------------------------ |
| Applications per user (week 1) | 10+                   | Phase 1                  |
| Weekly active users            | 2+ sessions/week      | Phase 1+                 |
| Status update rate             | > 50%                 | Phase 1+                 |
| Follow-up usage                | > 30% of users        | Phase 1+                 |
| Insights page visits           | > 25% of users        | Phase 2 (after P1 ships) |
| Known final state              | > 60% of applications | Phase 2+                 |
| Time to first value            | < 3 minutes           | Phase 1+                 |

### Acquisition Metrics (Phase 2+)

| Metric                                           | Target                |
| ------------------------------------------------ | --------------------- |
| Conversion rate (registrations / landing visits) | > 5%                  |
| Activation rate (5+ apps / registrations)        | > 30%                 |
| Week-1 retention (2+ sessions in week 2)         | > 40%                 |
| Referral rate                                    | Track (no target yet) |

### Hypothesis Validation Schedule

| #   | Hypothesis                                        | Validated in             |
| --- | ------------------------------------------------- | ------------------------ |
| H1  | Users manually input if form < 1 minute           | Phase 1 (VS-02, Week 3)  |
| H2  | Job descriptions yield 3+ recognized skills       | Phase 2 (VS-10, Week 12) |
| H3  | Tracking + insights > tracking alone (higher WAU) | Phase 2 (VS-11, Week 13) |
| H4  | Follow-ups reduce ghosting rate                   | Phase 1 (VS-04, Week 5)  |

---

## Future Backlog (P2)

Features for iteration after Open Beta, based on validated demand:

| Feature                      | Description                                       |
| ---------------------------- | ------------------------------------------------- |
| Temporal skill trends        | Frequency changes over last 30/60 days            |
| Recruiter contact management | Structured recruiter details per application      |
| Salary comparison            | Side-by-side offer comparison across applications |
| CSV export                   | Export application data for external use          |
| Filter by source/work mode   | Additional filter dimensions                      |
| Kanban board view            | Visual pipeline management                        |

---

## References

- [MVP Scope](MVP-SCOPE.md) — Priority classification (P0/P1/P2)
- [GTM Strategy](GTM-STRATEGY.md) — Launch phases and promotion channels
- [Epic Map](backlog/EPIC-MAP.md) — All epics and slices
- [Delivery Plan](backlog/SPRINTS.md) — Vertical slice delivery details
- [Global User Journey](backlog/JOURNEY.md) — End-to-end user flow
- [Metrics Plan](METRICS-PLAN.md) — Precise metric definitions and instrumentation
