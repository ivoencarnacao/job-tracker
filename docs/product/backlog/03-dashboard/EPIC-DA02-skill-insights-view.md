# EPIC-DA02: Skill Insights Display

**Bounded Context:** Dashboard (Supporting)
**Priority:** P1

---

## Feature SPEC

### Problem

Users need to see skill trends and market demand based on the jobs they are applying for. A dedicated Insights page helps guide their learning path and tailor their CV preparation by highlighting the most frequently requested skills in their specific target market.

### Business Rules

- **Skill Ranking:** Top skills are dynamically ranked by their frequency of appearance across the user's saved job applications.
- **Category Breakdown:** Skills must be grouped and counted by their respective categories (e.g., Languages, Frameworks, Tools) to provide structured insights.
- **Frequency Calculation:** The frequency is calculated and displayed as the percentage of total parsed applications that mention a specific skill.
- **Meaningful Threshold (Minimum Data):** A minimum of 5 applications with parsed descriptions is required to generate statistically meaningful insights.
- **Below Threshold State:** If the user has fewer than 5 parsed applications, the system must display an explanatory empty state encouraging them to add more detailed job descriptions to unlock the Insights feature.
- **Strict Data Isolation:** All skill aggregations and insights must be strictly scoped to the authenticated `userId`. The system must never aggregate cross-user data for personal insights.
- **Read-Only Context:** The Insights page is a pure read-only view. It aggregates data primarily from the Skills and Tracking contexts but performs no domain mutations.

### Ubiquitous Language

| Term                 | Definition                                                                                                                        |
| -------------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| Insights             | A dedicated dashboard page showing skill trends, category breakdowns, and market demand analysis tailored to the user's pipeline. |
| Meaningful Threshold | The minimum number of parsed applications (5) required to display relevant and statistically significant skill trends.            |

---

## Vertical Slices

| ID      | Slice                                                                            | Dependencies     |
| ------- | -------------------------------------------------------------------------------- | ---------------- |
| DA02-S1 | [Insights Page with Top Skills and Category Breakdown](DA02-S1-insights-page.md) | SK03-S1, SK01-S2 |
