# MVP Scope

## Must Have (P0)

These features are required for the MVP to deliver its core value.

| Feature | Description |
|---------|-------------|
| User registration and login | Email/password auth, secure sessions |
| Application CRUD | Create, read, update, delete job applications |
| Pipeline status management | 11 states (Saved through Withdrawn), status transitions |
| Follow-up reminders | Create, list pending, mark complete with outcome |
| Interview management | Record interviews linked to applications (type, date, notes, outcome) |
| Offer recording | Record offers linked to applications (salary, status, notes) |
| Dashboard with core metrics | Total count, count by status, response rate, pending follow-ups, upcoming interviews, no-response alerts |
| Search and filter | Text search by company/title, filter by status and date |

## Should Have (P1)

High-value features that strengthen the product but can ship shortly after initial launch.

| Feature | Description |
|---------|-------------|
| Manual skill tagging | Users can add skills to any application manually |
| Skill extraction from descriptions | Keyword matching against a built-in skill dictionary |
| Top skills display | Skills ranked by frequency in the Insights section |
| Skill category breakdown | Skills grouped by category (Language, Framework, Database, etc.) |
| Auto-ghosting suggestion | Notify user after 21 days without update on Applied/Under Review |
| Sort applications | By date, last update, or company name |

## Could Have (P2)

Nice-to-have features for later iteration within the MVP lifecycle.

| Feature | Description |
|---------|-------------|
| Temporal skill trends | Frequency changes over last 30/60 days |
| Recruiter contact management | Store recruiter details per application |
| Salary comparison across offers | Side-by-side comparison of offers across different applications |
| CSV export | Export application data for external use |
| Filter by source and work mode | Additional filter dimensions |

## Out of Scope

Explicitly excluded from the MVP to maintain focus.

- AI-powered CV or cover letter generation
- Automatic job applications
- LinkedIn, Indeed, or Glassdoor API integration
- Gmail or email auto-detection of responses
- Google Calendar or Outlook sync
- Advanced networking CRM
- Intelligent fit scoring
- Native mobile app (responsive web only)
- Multi-user collaboration
- OAuth or social login
- Kanban board view

---

## Hypotheses

| # | Hypothesis | How to Validate |
|---|-----------|-----------------|
| H1 | Users will manually input application data if the form takes less than 1 minute | Track time-to-save for first 100 applications; survey for friction feedback |
| H2 | Pasting job descriptions provides enough text for useful skill extraction | Measure % of descriptions yielding 3+ recognized skills |
| H3 | Tracking + skill insights together create higher retention than tracking alone | Compare WAU between users who visit Insights vs. those who don't |
| H4 | Follow-up reminders reduce the number of applications left in limbo | Compare "Ghosted" rate between users who use follow-ups vs. those who don't |

---

## MVP Metrics

> For precise definitions, formulas, and instrumentation details, see [Metrics Plan](METRICS-PLAN.md).

| Metric | Target | Measurement |
|--------|--------|-------------|
| Applications per user (week 1) | 10+ | Count applications created within 7 days of registration |
| Weekly active users | 2+ sessions/week | Session count per user per week |
| Status update rate | >50% | Applications with at least one status change / total applications |
| Follow-up usage | >30% of users | Users who created at least one follow-up |
| Insights page visits | >25% of users | Users who visited the Insights section at least once |
| Known final state | >60% of applications | Applications in a terminal state (Offer, Rejected, Ghosted, Withdrawn) |
| Time to first value | < 3 minutes | Time from registration to first application + dashboard view |

---

## Release Criteria

### Private Beta

The MVP is ready for Private Beta (10-20 testers) when all of the following are met:

**Functional:**

- [ ] Sprints 1-4 complete (all P0 slices implemented and tested)
- [ ] All acceptance criteria for P0 slices verified
- [ ] Dashboard displays correct metrics with real data
- [ ] End-to-end flow works: register → login → create application → update status → view dashboard

**Technical:**

- [ ] JaCoCo ≥ 70% line coverage
- [ ] Zero critical vulnerabilities (CSRF, XSS, SQL injection, IDOR)
- [ ] Performance: dashboard < 2s, form submission < 500ms
- [ ] Flyway migrations V1-V5 execute without errors

**Operational:**

- [ ] Functional deploy on staging environment
- [ ] Database backup configured
- [ ] In-app feedback widget integrated (simple text form)
- [ ] Analytics (Plausible/Umami) configured and capturing events
