# EPIC-TR02: Pipeline Management

**Bounded Context:** Tracking (Core)
**Priority:** P0

---

## Feature SPEC

### Problem

Users need to update the application status as the hiring process progresses. The pipeline must enforce valid transitions to maintain data integrity and reflect real-world hiring processes, preventing impossible or illogical state jumps (e.g., moving from a terminal rejection back to an interview).

### Business Rules

- `ApplicationStatus` has 11 states: `SAVED`, `APPLIED`, `UNDER_REVIEW`, `HR_INTERVIEW`, `TECHNICAL_INTERVIEW`, `CHALLENGE`, `FINAL_INTERVIEW`, `OFFER`, `REJECTED`, `GHOSTED`, `WITHDRAWN`.
- States are not strictly sequential — users can skip stages (e.g., transitioning from `SAVED` directly to `TECHNICAL_INTERVIEW` is valid).
- Terminal states: `REJECTED` and `WITHDRAWN` are final (no transitions out allowed).
- `GHOSTED` is a reversible state: it can transition to any active state if the employer eventually responds.
- `OFFER` can transition to `REJECTED` or `WITHDRAWN` (e.g., employer rescinds the offer or the user declines it).
- A domain service (`PipelineService`) or the Aggregate Root itself enforces these valid state transitions.
- Any status change automatically updates the `updated_at` timestamp of the application.
- *(P1)* A successful status change must publish the `ApplicationStatusChanged` domain event to notify other contexts (e.g., Analytics, Notifications).

### Ubiquitous Language

| Term            | Definition                                                                                                              |
| --------------- | ----------------------------------------------------------------------------------------------------------------------- |
| Pipeline        | The logical sequence of stages an application goes through, from the initial save to a terminal state.                  |
| PipelineService | A domain service responsible for enforcing valid state transitions when the logic is extracted from the Aggregate Root. |
| Terminal State  | A final status from which no further transitions are allowed (e.g., Rejected, Withdrawn).                               |

---

## Vertical Slices

| ID      | Slice                                                                         | Dependencies |
| ------- | ----------------------------------------------------------------------------- | ------------ |
| TR02-S1 | [Pipeline State Machine and Status Update](TR02-S1-pipeline-state-machine.md) | TR01-S1      |
