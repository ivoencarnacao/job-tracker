# Slice SK03-S1: Skill Frequency and Top Skills Computation

**Epic:** [EPIC-SK03](EPIC-SK03-skill-insights.md)
**Priority:** P1
**Sprint:** 6

---

## 🎯 Value Delivered

The system can accurately compute which skills appear most often across the user's applications. This computation provides the robust data foundation necessary to power the visual Insights page.

## 🔗 Dependencies

- **SK01-S2** — Skills must be successfully associated with applications (`job_application_skills`) to perform meaningful aggregations.

---

## 📖 User Story

As an **authenticated User**, I want **the system to analyze my tagged and extracted skills**, so that **I can discover my personal skill trends and category breakdowns based on real data**.

---

## ✅ Acceptance Criteria

**Scenario 1: Meaningful Data Threshold**

- Before computing insights, the system verifies if the user meets the minimum threshold of 5 applications with descriptions for meaningful results.

**Scenario 2: Accurate Frequency Calculation**

- The system calculates the skill frequency using the strict formula: count of applications mentioning the skill divided by the total applications with descriptions.

**Scenario 3: Strict Data Isolation (Invariant)**

- All aggregations and read-only queries are strictly scoped to the authenticated user's `userId`. Cross-user data contamination is strictly prohibited.

---

## 🛠️ Technical Tasks (Implementation)

### Domain

- [ ] Create `skills/domain/TrendAnalyzer.java` (Domain Service).
  - Encapsulate the logic that computes frequency, top-N rankings, and category grouping from the raw repository data.

### Infrastructure

- [ ] Add read-only aggregate queries to the repository:
  - `countBySkillAndUserId`.
  - `findTopSkillsByUserId(limit)` (aggregates on `job_application_skills` joined with `skills`).
  - `countByCategory`.

### Application

- [ ] Create DTOs:
  - `skills/application/dto/SkillFrequencyOutput.java`.
  - `skills/application/dto/CategoryBreakdownOutput.java`.
- [ ] Create `skills/application/GetTopSkillsUseCase.java`.
  - Orchestrates the call to the repository and the `TrendAnalyzer`, returning a ranked skill list with calculated frequency percentages.
- [ ] Create `skills/application/GetCategoryBreakdownUseCase.java`.
  - Returns category-grouped counts utilizing the `TrendAnalyzer`.
