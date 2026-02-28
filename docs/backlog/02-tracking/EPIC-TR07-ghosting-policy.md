# EPIC-TR07: Auto-Ghosting Suggestion

**Bounded Context:** Tracking (Core)
**Priority:** P1

---

## Feature SPEC

### Problem

Users often lose track of job applications that never receive a response from the employer. To maintain an accurate pipeline and reduce cognitive load, the system should proactively identify stale applications and suggest marking them as `GHOSTED`, without ever mutating the status automatically.

### Business Rules

- **GhostingPolicy**: An application residing in the `APPLIED` or `UNDER_REVIEW` state for 21 or more days without any status update is flagged as a ghosting candidate.
- **User Agency (Invariant)**: The policy generates a **suggestion only**. The system must never automatically mutate the `ApplicationStatus` to `GHOSTED`. The user always retains the final decision.
- **Dismissal**: A user can explicitly dismiss a ghosting suggestion for a specific application, preventing it from being flagged again for the same pipeline stage.
- **NoResponseAlert**: A lighter dashboard indicator (warning) is triggered at 14 days of inactivity, distinct from the formal ghosting suggestion.
- **Reversibility**: As established in the state machine (TR02), if an application is marked as `GHOSTED` and the employer subsequently responds, the status can be reversed to any active state.
- **Evaluation Mechanism**: The detection can be evaluated dynamically on read (via database queries) or asynchronously via a scheduled background job that identifies candidates.

### Ubiquitous Language

| Term                | Definition                                                                                                                                 |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| GhostingPolicy      | A domain service or rule encapsulating the temporal logic (21 days) to identify stale applications.                                        |
| GhostingSuggestion  | The actionable recommendation presented to the user to transition an application to the `GHOSTED` state.                                   |
| NoResponseAlert     | A non-actionable visual dashboard warning indicating an application has been inactive for 14+ days.                                        |
| DismissedSuggestion | A state where the user has explicitly chosen to ignore a ghosting suggestion, silencing future alerts for that specific application stage. |

---

## Vertical Slices

| ID      | Slice                                                       | Dependencies     |
| ------- | ----------------------------------------------------------- | ---------------- |
| TR07-S1 | [Ghosting Policy Detection](TR07-S1-ghosting-detection.md)  | TR01-S1, TR02-S1 |
| TR07-S2 | [Ghosting Suggestion UI](TR07-S2-ghosting-suggestion-ui.md) | TR07-S1, DA01-S1 |
