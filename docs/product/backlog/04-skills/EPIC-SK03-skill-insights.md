# EPIC-SK03: Skill Insights

**Bounded Context:** Skills (Core)
**Priority:** P1

---

## Feature SPEC

### Problem

Users want to deeply understand which skills the market demands most, allowing them to focus their learning efforts and tailor their CV preparation effectively.

### Business Rules

- **Top Skills Ranking:** Skills must be ranked dynamically by their frequency of appearance across all of the user's applications.
- **Category Breakdown:** Skills are visually and logically grouped by their `SkillCategory`, accompanied by exact occurrence counts.
- **Frequency Calculation:** Skill frequency is calculated as the percentage of the user's total parsed applications that mention each specific skill.
- **Meaningful Threshold:** A minimum threshold of 5 applications containing job descriptions is strictly required to compute and surface meaningful insights.
- **Below Threshold State:** If the user falls below this threshold, the system must halt computation and present a clear explanatory message.

### Ubiquitous Language

| Term          | Definition                                                                                                                                        |
| ------------- | ------------------------------------------------------------------------------------------------------------------------------------------------- |
| TrendAnalyzer | A domain service responsible for computing skill frequencies, generating top-N lists, and performing category groupings based on the user's data. |
| Trend         | A statistical insight or aggregated metric derived from the raw job skill data.                                                                   |
| ResponseRate  | The percentage of applications that have successfully received an active employer response.                                                       |

---

## Vertical Slices

| ID      | Slice                                                                    | Dependencies |
| ------- | ------------------------------------------------------------------------ | ------------ |
| SK03-S1 | [Skill Frequency and Top Skills Computation](SK03-S1-skill-frequency.md) | SK01-S2      |
