# Slice DA01-S1: Application Count Metrics

**Epic:** [EPIC-DA01](EPIC-DA01-core-metrics.md)
**Priority:** P0
**Vertical Slice:** VS-07

---

## 🎯 Value Delivered

An authenticated User can see their total applications, a breakdown by status, and the number of applications submitted this week. This makes the dashboard useful from the very first entry, providing immediate positive reinforcement and a clear overview of their job hunt activity.

## 🔗 Dependencies

- **TR01-S1** — Applications must exist in the Tracking context to be counted.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce strict data isolation.

---

## 📖 User Story

As an **authenticated User**, I want to **view a summary of my core application metrics on the dashboard**, so that **I can understand my overall job search progress at a glance**.

---

## ✅ Acceptance Criteria

**Scenario 1: Successful Metrics Display**

- The user navigates to the dashboard.
- The system retrieves the metrics: total applications, a map of counts grouped by `ApplicationStatus`, and the count of applications created in the last 7 days.
- The UI displays these metrics clearly using summary cards or charts.

**Scenario 2: Empty State and Onboarding**

- The user navigates to the dashboard but has exactly 0 total applications.
- Instead of showing empty metric cards or zero-values, the UI displays a welcoming "Empty State" block.
- The empty state includes a clear Call-To-Action (CTA) button encouraging the user to "Add your first application to get started".

**Scenario 3: Strict Data Isolation (Invariant)**

- All read operations are strictly scoped to the authenticated user's `userId`.
- The dashboard must never expose aggregate counts that include other users' data.

**Scenario 4: Pure Read-Only Operation (CQRS Pattern)**

- The dashboard operations do not load the heavy `JobApplication` Aggregate Roots into memory.
- It uses highly optimized read-only queries against the database (or via an internal Open Host Service/API provided by the Tracking context) to aggregate the counts.
- No domain state is mutated.

---

## 🛠️ Technical Tasks (Implementation)

### Infrastructure

- [ ] Create `dashboard/infrastructure/DashboardQueryRepository.java`
  - Implement read-only queries directly against the underlying tables (or a designated read model):
    - `countByUserId(UUID userId)`
    - `countByUserIdGroupByStatus(UUID userId)` (Returns a Map/Projection)
    - `countByUserIdAndCreatedAfter(UUID userId, OffsetDateTime date)`

### Application

- [ ] Create `dashboard/application/dto/DashboardMetricsOutput.java` (DTO)
  - Fields: `long totalCount`, `Map<String, Long> countByStatus`, `long applicationsThisWeek`.
- [ ] Create `dashboard/application/GetDashboardMetricsUseCase.java`
  - Extracts the `userId` from the `AuthenticatedUser`.
  - Orchestrates the calls to the `DashboardQueryRepository`.
  - Assembles and returns the `DashboardMetricsOutput` DTO.

### Web / UI

- [ ] Create `dashboard/web/DashboardController.java`
  - `GET /dashboard` endpoint.
  - Calls the Use Case and injects the DTO into the view model.
- [ ] Create `templates/dashboard/dashboard.html`
  - Implement conditional rendering (Thymeleaf `th:if`):
    - If `totalCount == 0`, render the Empty State and CTA button.
    - If `totalCount > 0`, render the Metric Cards (Total, By-Status breakdown, This Week).
