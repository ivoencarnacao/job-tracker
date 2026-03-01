# EPIC-TR03: Follow-Up Reminders

**Bounded Context:** Tracking (Core)
**Priority:** P0

---

## Feature SPEC

### Problem

Applications often go stale when users forget to follow up with recruiters or hiring managers. `FollowUps` are first-class domain concepts within the `JobApplication` aggregate that help users maintain momentum and visibility in their job search.

### Business Rules

- A `FollowUp` belongs to exactly one `JobApplication` (it is a child entity governed by the aggregate).
- Multiple `FollowUps` can exist per application.
- A `FollowUp` must have a due date (required). Notes are optional. The completed flag defaults to false.
- `FollowUpOutcome` has two states: `RESPONDED` or `NO_RESPONSE`.
- Past-date follow-ups are allowed (retroactive recording) and must appear immediately as overdue in the UI.
- Marking a follow-up as completed strictly requires selecting an outcome.
- **Aggregate Enforcement:** All write operations (create, complete, update) must go through the `JobApplication` aggregate root to ensure consistency. `FollowUps` cannot be fetched or saved independently of their parent application.
- *(P1)* Key actions publish domain events (e.g., `FollowUpScheduled`, `FollowUpCompleted`) to allow downstream contexts (like a Notifications module) to react accordingly.

### Ubiquitous Language

| Term            | Definition                                                                                                     |
| --------------- | -------------------------------------------------------------------------------------------------------------- |
| FollowUp        | An internal reminder attached to an application, containing a due date, completion status, and optional notes. |
| FollowUpOutcome | The registered result of a completed follow-up: `RESPONDED` or `NO_RESPONSE`.                                  |
| Overdue         | A follow-up state calculated dynamically when the due date is in the past and the completed flag is false.     |

---

## Vertical Slices

| ID      | Slice                                                              | Dependencies |
| ------- | ------------------------------------------------------------------ | ------------ |
| TR03-S1 | [Create and List Follow-Ups](TR03-S1-create-list-follow-ups.md)    | TR01-S1      |
| TR03-S2 | [Complete a Follow-Up with Outcome](TR03-S2-complete-follow-up.md) | TR03-S1      |
