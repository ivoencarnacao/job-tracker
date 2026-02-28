# EPIC-DA01: Core Dashboard Metrics

**Bounded Context:** Dashboard (Supporting)
**Priority:** P0

---

## Feature SPEC

### Problem

Users need a summary view to understand their job search progress at a glance. The dashboard is the first screen users see after login, providing critical visibility into their hiring pipeline, upcoming tasks, and overall success rate.

### Business Rules

- Metrics required: total applications, count by status, applications added this week, response rate, upcoming interviews (next 7 days), pending follow-ups (due/overdue), and no-response alerts (14+ days in `APPLIED` or `UNDER_REVIEW`).
- **Strict Data Isolation:** All metrics must be strictly scoped to the authenticated `userId`.
- **Empty State:** If the user has no applications, the dashboard must display a clear call-to-action ("Add your first application to get started").
- **Read-Only Context:** The Dashboard is strictly a read-only context. It performs no mutations to any aggregate and publishes no domain events. It only queries and aggregates data optimized for presentation.
- **Rendering:** The dashboard is server-rendered and refreshes dynamically on page load.
- **Calculation Logic (Response Rate):** `ResponseRate` = applications with an employer response (status beyond `APPLIED` and `UNDER_REVIEW`) / total applications (excluding those with a `SAVED` status).

### Ubiquitous Language

| Term            | Definition                                                                                                           |
| --------------- | -------------------------------------------------------------------------------------------------------------------- |
| Dashboard       | A read-only overview of aggregate metrics and upcoming tasks tailored to the user.                                   |
| ResponseRate    | The percentage of submitted applications that have received an active employer response.                             |
| NoResponseAlert | A non-actionable dashboard warning indicating an application has been inactive for 14+ days without a status change. |

---

## Vertical Slices

| ID      | Slice                                                                   | Dependencies              |
| ------- | ----------------------------------------------------------------------- | ------------------------- |
| DA01-S1 | [Application Count Metrics](DA01-S1-count-metrics.md)                   | TR01-S1                   |
| DA01-S2 | [Response Rate and Activity Metrics](DA01-S2-response-rate-activity.md) | DA01-S1, TR03-S1, TR04-S1 |
| DA01-S3 | [No-Response Alerts](DA01-S3-no-response-alerts.md)                     | DA01-S1                   |
