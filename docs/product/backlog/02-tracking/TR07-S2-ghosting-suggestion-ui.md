# Slice TR07-S2: Ghosting Suggestion UI

**Epic:** [EPIC-TR07](EPIC-TR07-ghosting-policy.md)
**Priority:** P1
**Sprint:** 6

---

## 🎯 Value Delivered

An authenticated User sees notifications for stale applications and can act on them directly from the dashboard. This reduces cognitive load and keeps the hiring pipeline accurate without forcing unwanted or surprising automatic changes.

## 🔗 Dependencies

- **TR07-S1** — Detection logic (`DetectGhostingCandidatesUseCase`) must exist to supply the data.
- **DA01-S1** — The Dashboard must exist to display the suggestions.
- **TR02-S1** — Relies on the existing `UpdatePipelineStatusUseCase` to apply the status change safely.

---

## 📖 User Story

As an **authenticated User**, I want to **see alerts for stale applications on my dashboard**, so that **I can quickly mark them as ghosted or dismiss the notification**.

---

## ✅ Acceptance Criteria

**Scenario 1: Displaying Ghosting Suggestions**

- When the user loads the dashboard, the system retrieves ghosting candidates using the detection use case.
- The UI displays a clear, non-intrusive alert or notification for each candidate (e.g., "No update for 21 days — mark as ghosted?").

**Scenario 2: Dismissing the Suggestion**

- The user chooses to dismiss or ignore a specific ghosting suggestion.
- The system records this dismissal (e.g., by updating a flag or recording an interaction) so the user isn't repeatedly spammed for the same application stage.
- The application's pipeline status remains completely unchanged.

**Scenario 3: Accepting the Suggestion (Mark as Ghosted)**

- The user clicks the "Mark as Ghosted" quick action on the notification.
- The system routes this action through the existing `UpdatePipelineStatusUseCase` with the target status set to `GHOSTED`.
- The standard state machine transition rules are strictly enforced, and the `ApplicationStatusChanged` event is published.
- The notification is permanently cleared from the dashboard.

---

## 🛠️ Technical Tasks (Implementation)

### Application

- [ ] Create `tracking/application/DismissGhostingSuggestionUseCase.java` (Optional but recommended)
  - Implements the logic to record that a user dismissed the alert for a specific application at its current status, preventing the query from fetching it again.

### Web / UI

- [ ] Update `DashboardController` (or the relevant dashboard view model builder)
  - Inject and call `DetectGhostingCandidatesUseCase`.
  - Add the returned candidates to the dashboard model.
  - Implement a `POST` or `PATCH` endpoint for the "Dismiss" action.
- [ ] Update dashboard templates
  - Create an alert banner or notification list UI component.
  - Add a "Dismiss" button/link.
  - Add a "Mark as Ghosted" quick-action button (this should ideally trigger a form submission pointing to the existing `PATCH /applications/{id}/status` endpoint established in TR02-S1).
