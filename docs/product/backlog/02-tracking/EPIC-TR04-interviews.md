# EPIC-TR04: Interview Management

**Bounded Context:** Tracking (Core)
**Priority:** P0

---

## Feature SPEC

### Problem

Users need to record and manage interviews to prepare effectively and track their outcomes. Interviews represent critical milestones in the hiring pipeline and are modelled as child entities of the `JobApplication` aggregate.

### Business Rules

- An `Interview` belongs to exactly one `JobApplication` (a child entity strictly governed by the aggregate root).
- Multiple `Interviews` can exist per application (typically one per pipeline stage).
- Required fields: scheduled date/time, type (`HR`, `TECHNICAL`, `FINAL`), and outcome (defaults to `PENDING`).
- Optional fields: format (`ONLINE`, `ON_SITE`), interviewer names/details, and preparation notes.
- **Crucial Decoupling:** Creating or updating an interview does NOT automatically change the `JobApplication` status. The user remains in full manual control of the pipeline state machine.
- `InterviewOutcome` has three valid states: `PENDING`, `PASSED`, or `FAILED`.
- **Aggregate Enforcement:** All write operations (create, update, outcome registration) must go through the `JobApplication` aggregate root to ensure absolute data consistency.
- Key actions publish domain events (e.g., `InterviewScheduled`, `InterviewUpdated`) to allow downstream contexts (like a Calendar Integration or Notifications module) to react asynchronously.

### Ubiquitous Language

| Term             | Definition                                                                                   |
| ---------------- | -------------------------------------------------------------------------------------------- |
| Interview        | A scheduled meeting with recruiters or team members, tracking its type, format, and outcome. |
| InterviewType    | Categorization of the interview stage: `HR`, `TECHNICAL`, or `FINAL`.                        |
| InterviewFormat  | The medium of the interview: `ONLINE` or `ON_SITE`.                                          |
| InterviewOutcome | The evaluated result of the interview: `PENDING`, `PASSED`, or `FAILED`.                     |

---

## Vertical Slices

| ID      | Slice                                                                           | Dependencies |
| ------- | ------------------------------------------------------------------------------- | ------------ |
| TR04-S1 | [Create, List, and Update Interviews](TR04-S1-create-list-update-interviews.md) | TR01-S1      |
