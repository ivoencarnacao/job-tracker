# Slice DA01-S2: Response Rate and Activity Metrics

**Epic:** [EPIC-DA01](EPIC-DA01-core-metrics.md)
**Priority:** P0
**Vertical Slice:** VS-07

---

## 🎯 Value Delivered

An authenticated User can see their overall response rate, upcoming interviews, and pending follow-ups directly on the dashboard. This transforms the dashboard from a simple summary into an actionable workspace, providing immediate awareness of critical next steps and real success metrics.

## 🔗 Dependencies

- **DA01-S1** — The base dashboard infrastructure and UI must exist.
- **TR03-S1** — Follow-ups must exist to populate the pending tasks list.
- **TR04-S1** — Interviews must exist to populate the upcoming interviews list.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce strict data isolation.

---

## 📖 User Story

As an **authenticated User**, I want to **view my application response rate and upcoming activities (interviews and follow-ups)**, so that **I know exactly what tasks require my immediate attention and how successful my applications are**.

---

## ✅ Acceptance Criteria

**Scenario 1: Accurate Response Rate Calculation**

- The system calculates the user's `ResponseRate` as a percentage.
- **Denominator:** Total number of applications where the status is NOT `SAVED` (i.e., actually submitted applications).
- **Numerator:** Total number of applications where the status implies an employer response (status is NOT `SAVED`, `APPLIED`, `UNDER_REVIEW`, or `GHOSTED`).
- If the denominator is 0, the system displays a default state (e.g., "N/A" or "0%") without throwing a division-by-zero error.

**Scenario 2: Upcoming Interviews List**

- The system retrieves all interviews associated with the user's applications where the `scheduled_at` date falls between now and `now + 7 days`.
- The list is displayed on the dashboard, ordered by the soonest date first.

**Scenario 3: Pending Follow-Ups List**

- The system retrieves all follow-ups associated with the user's applications where `completed = false` AND the `due_date` is less than or equal to `now + 7 days`.
- Overdue follow-ups (`due_date < now`) are visually highlighted to prompt immediate action.

**Scenario 4: Strict Data Isolation (Invariant)**

- All read operations are strictly scoped to the authenticated user's `userId`. Because `Interview` and `FollowUp` are child entities, the queries must correctly join with the parent `JobApplication` to verify ownership.
- The dashboard operations remain strictly read-only and do not mutate any domain state.

---

## 🛠️ Technical Tasks (Implementation)

### Infrastructure

- [ ] Update `dashboard/infrastructure/DashboardQueryRepository.java`
  - Add `calculateResponseRate(UUID userId)`: Executes a highly optimized native or JPQL query to calculate the ratio based on the specific status inclusions/exclusions.
  - Add `findUpcomingInterviews(UUID userId, OffsetDateTime from, OffsetDateTime to)`: Joins the `interviews` table with `job_applications` to filter by `userId` and date range.
  - Add `findPendingFollowUps(UUID userId, LocalDate thresholdDate)`: Joins the `follow_ups` table with `job_applications` to filter by `userId`, `completed = false`, and date range.

### Application

- [ ] Update `dashboard/application/dto/DashboardMetricsOutput.java` (DTO)
  - Add fields: `double responseRate`, `List<UpcomingActivityDTO> upcomingInterviews`, `List<UpcomingActivityDTO> pendingFollowUps`.
- [ ] Update `dashboard/application/GetDashboardMetricsUseCase.java`
  - Orchestrate the new calls to the `DashboardQueryRepository`.
  - Handle the edge case for `ResponseRate` division-by-zero calculation gracefully.
  - Map the raw query results into the DTO structures.

### Web / UI

- [ ] Update `dashboard/web/DashboardController.java`
  - No new endpoints needed; simply ensure the extended DTO is passed to the view model.
- [ ] Update `templates/dashboard/dashboard.html`
  - Add a new metric card for "Response Rate" (format as a percentage).
  - Add an "Upcoming Activities" section.
  - Render lists for Interviews and Follow-ups, applying specific CSS classes for overdue tasks.
