# Slice DA02-S1: Insights Page with Top Skills and Category Breakdown

**Epic:** [EPIC-DA02](EPIC-DA02-skill-insights-view.md)
**Priority:** P1
**Vertical Slice:** VS-11

---

## 🎯 Value Delivered

An authenticated User can see exactly which skills are most in-demand across their saved job postings, visually grouped by category with frequency percentages. This transforms a simple tracking tool into a powerful, personalized career strategy advisor, acting as a key differentiator for the product.

## 🔗 Dependencies

- **SK03-S1** — Skill computation and extraction logic must exist.
- **SK01-S2** — The associations between skills and job applications must be established in the database.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce strict data isolation.

---

## 📖 User Story

As an **authenticated User**, I want to **view a dedicated Insights page showing my most frequently required skills and their category breakdown**, so that **I can prioritize my learning and tailor my CV to market demands**.

---

## ✅ Acceptance Criteria

**Scenario 1: Meaningful Threshold (Below Threshold State)**

- The user navigates to the Insights page.
- The system evaluates the user's total number of applications that contain parsed job descriptions.
- If the count is strictly less than 5, the system blocks the rendering of empty or skewed charts.
- The UI displays a clear, explanatory message: "Add more applications with job descriptions to unlock meaningful skill insights".

**Scenario 2: Successful Skill Insights Display**

- The user has 5 or more parsed applications and navigates to the Insights page.
- The system calculates the top skills, ranking them by their frequency of appearance across the user's applications.
- The frequency is displayed accurately as a percentage (e.g., "Python appears in 60% of your saved jobs").
- The UI presents a visual category breakdown (e.g., Languages vs. Frameworks vs. Tools) using a bar chart or equivalent visual representation.

**Scenario 3: Strict Data Isolation (Invariant)**

- All read operations and frequency aggregations are strictly scoped to the authenticated user's `userId`.
- The system never aggregates or leaks cross-user skill data into a personal Insights dashboard.

**Scenario 4: Pure Read-Only Operation (CQRS Pattern)**

- The Insights operations rely exclusively on read-only queries against the `skills` and `job_application_skills` tables (via an internal Open Host Service or direct read-model query).
- No domain state is mutated during the generation of these insights.

---

## 🛠️ Technical Tasks (Implementation)

### Infrastructure

- [ ] Create `dashboard/infrastructure/SkillInsightsQueryRepository.java`
  - Implement read-only queries to join `core.job_applications`, `skills.job_application_skills`, and `skills.skills`.
  - Calculate frequency counts and group by skill category directly in the database (or via an optimized read-model view).

### Application

- [ ] Create `dashboard/application/dto/SkillInsightsOutput.java` (DTO)
  - Fields: `List<SkillFrequencyDTO> topSkills`, `Map<String, Integer> categoryBreakdown`, `boolean hasEnoughData`.
- [ ] Create `dashboard/application/GetSkillInsightsUseCase.java`
  - Extract `userId` from `AuthenticatedUser`.
  - Check the 5-application threshold first.
  - If the threshold is met, delegate to the `SkillInsightsQueryRepository` to fetch the data.
  - Assemble and return the `SkillInsightsOutput` DTO.

### Web / UI

- [ ] Create `dashboard/web/InsightsController.java`
  - `GET /insights` endpoint.
  - Call the use case and inject the resulting DTO into the view model.
- [ ] Create `templates/dashboard/insights.html`
  - Implement conditional rendering based on the `hasEnoughData` flag.
  - If `false`: Render the below-threshold state message and a CTA to add more applications.
  - If `true`: Render the ranked skills list (with percentages) and the CSS/HTML-based category bar chart.
