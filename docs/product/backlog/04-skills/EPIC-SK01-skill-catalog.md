# EPIC-SK01: Skill Catalog & Manual Tagging

**Bounded Context:** Skills (Core)
**Priority:** P1

---

## Feature SPEC

### Problem

Users want to know which skills are genuinely demanded by the market based on the roles they are applying for. Before introducing complex automated extraction or parsing, the system needs a robust foundational skill catalog and the ability for users to manually tag their applications with relevant skills to ensure data accuracy.

### Business Rules

- **Catalog Structure:** A `Skill` entity has a canonical name (which must be strictly unique), a specific `SkillCategory`, and a collection of aliases.
- **Categories:** The allowed categories are restricted to: `LANGUAGE`, `FRAMEWORK`, `DATABASE`, `CLOUD`, `DEVOPS`, `TESTING`, `ARCHITECTURE`, `TOOL`, `METHODOLOGY`, and `SOFT_SKILL`.
- **Alias Mapping:** Aliases map many-to-one to a canonical `Skill` (e.g., "Postgres", "psql", and "PostgreSQL DB" all resolve seamlessly to the canonical "PostgreSQL").
- **Association:** `JobSkill` represents the many-to-many association between a `JobApplication` (from the Tracking context) and a cataloged `Skill`.
- **Idempotency:** Each skill is counted exactly once per application, regardless of how many times it might be mentioned or how many of its aliases are matched.
- **Seed Data:** To provide immediate value, the skill dictionary must be pre-seeded (e.g., via Flyway migrations) with common tech skills (~100 entries).

### Ubiquitous Language

| Term          | Definition                                                                                                                                                 |
| ------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Skill         | A professional competency or technology defined by a unique canonical name, a primary category, and optional search aliases.                               |
| SkillCategory | The domain grouping of a skill: `LANGUAGE`, `FRAMEWORK`, `DATABASE`, `CLOUD`, `DEVOPS`, `TESTING`, `ARCHITECTURE`, `TOOL`, `METHODOLOGY`, or `SOFT_SKILL`. |
| JobSkill      | The cross-context association (join entity) linking a tracked `JobApplication` to a cataloged `Skill`.                                                     |

---

## Vertical Slices

| ID      | Slice                                                                   | Dependencies     |
| ------- | ----------------------------------------------------------------------- | ---------------- |
| SK01-S1 | [Skill Catalog Seed Data](SK01-S1-skill-catalog-seed.md)                | ID01-S1          |
| SK01-S2 | [Manual Skill Tagging on Applications](SK01-S2-manual-skill-tagging.md) | SK01-S1, TR01-S1 |
