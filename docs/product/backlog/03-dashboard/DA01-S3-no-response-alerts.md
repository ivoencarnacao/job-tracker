# Slice DA01-S3: No-Response Alerts

**Epic:** [EPIC-DA01](EPIC-DA01-core-metrics.md)
**Priority:** P0
**Vertical Slice:** VS-07

---

## 🎯 Value Delivered

An authenticated User is proactively warned about applications that may be going stale (14+ days without any update). This provides an early indicator to follow up, distinct from the more severe 21-day "Ghosting" policy suggestion, keeping the user engaged with their active pipeline.

## 🔗 Dependencies

- **DA01-S1** — The base dashboard infrastructure and UI must exist to host the alerts.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce strict data isolation.

---

## 📖 User Story

As an **authenticated User**, I want to **see early warnings on my dashboard for applications that haven't had an update in 14 days**, so that **I can decide whether to proactively reach out to the employer before the opportunity goes cold**.

---

## ✅ Acceptance Criteria

**Scenario 1: Accurate Stale Detection (14-Day Threshold)**

- The system evaluates the user's applications to find those currently in the `APPLIED` or `UNDER_REVIEW` states.
- An application is flagged for a "No-Response Alert" if its `updated_at` timestamp is older than 14 days.
- This logic must remain entirely distinct from the 21-day Ghosting Policy (TR07).

**Scenario 2: Strict Data Isolation (Invariant)**

- The detection query is strictly scoped to the authenticated user's `userId`.
- It is impossible for the system to evaluate or return alerts belonging to another user.

**Scenario 3: Read-Only Alert Presentation**

- The system displays a count and a list of these stale applications in a dedicated alert section on the dashboard.
- Each item in the list includes a direct link to the application's detail page for easy navigation.
- The dashboard operation is purely read-only; generating this alert does not mutate the `ApplicationStatus` or any domain state.

---

## 🛠️ Technical Tasks (Implementation)

### Infrastructure

- [ ] Update `dashboard/infrastructure/DashboardQueryRepository.java`
  - Add a read-only query: `findNoResponseAlerts(UUID userId, OffsetDateTime thresholdDate)`.
  - The query should filter by `userId`, `status IN ('APPLIED', 'UNDER_REVIEW')`, and `updated_at < :thresholdDate` (where threshold is `now - 14 days`).

### Application

- [ ] Update `dashboard/application/dto/DashboardMetricsOutput.java` (DTO)
  - Add fields: `long noResponseAlertCount`, `List<NoResponseAlertDTO> noResponseAlerts`.
- [ ] Update `dashboard/application/GetDashboardMetricsUseCase.java`
  - Calculate the 14-day threshold date.
  - Call the `findNoResponseAlerts` query repository method.
  - Map the results into the `DashboardMetricsOutput` DTO.

### Web / UI

- [ ] Update `templates/dashboard/dashboard.html`
  - Add a dedicated "Alerts" or "Needs Attention" section.
  - Render the list of stale applications, providing the company name, job title, and the number of days waiting.
  - Ensure each alert item wraps an `<a>` tag linking to `/applications/{id}`.
