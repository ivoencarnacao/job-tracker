# Slice TR03-S2: Complete a Follow-Up with Outcome

**Epic:** [EPIC-TR03](EPIC-TR03-follow-ups.md)
**Priority:** P0
**Vertical Slice:** VS-04

---

## 🎯 Value Delivered

An authenticated User can mark follow-ups as done and record the outcome of the interaction. This ensures completed follow-ups no longer clutter the pending list, providing a clear sense of progression and maintaining an accurate history of recruiter communications.

## 🔗 Dependencies

- **TR03-S1** — A `FollowUp` must exist within a `JobApplication` to be completed.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce ownership.

---

## 📖 User Story

As an **authenticated User**, I want to **mark a pending follow-up as complete by recording its outcome**, so that **I can clear my pending tasks and track who has responded to me**.

---

## ✅ Acceptance Criteria

**Scenario 1: Successful Follow-Up Completion**

- The user selects a pending follow-up and chooses a valid outcome (`RESPONDED` or `NO_RESPONSE`).
- The system delegates the operation to the `JobApplication` aggregate, which locates the specific `FollowUp` in its internal collection.
- The system sets the `FollowUp`'s `completed` flag to `true` and assigns the chosen outcome.
- The updated aggregate is successfully persisted to the database.
- The UI updates, moving the follow-up from the pending view to a historical/completed view (or visually crossing it out).

**Scenario 2: Enforce Aggregate Root Rules and Ownership**

- The system strictly verifies that the parent `JobApplication` belongs to the currently authenticated user (`userId` check).
- The system enforces that an outcome is strictly required to mark a follow-up as complete.
- The `FollowUp` is never updated directly via an independent repository; the state change must pass through the `JobApplication.completeFollowUp(...)` method to guarantee aggregate consistency.

**Scenario 3: Domain Event Publication**

- Upon successfully persisting the aggregate with the completed follow-up, the system publishes a `FollowUpCompleted` domain event (including the `followUpId`, `applicationId`, and `outcome`).

---

## 🛠️ Technical Tasks (Implementation)

### Domain

- [ ] Update `tracking/domain/JobApplication.java` (Aggregate Root)
  - Add `completeFollowUp(UUID followUpId, FollowUpOutcome outcome)` method.
  - The method must find the correct child entity, apply the changes (set `completed = true` and update the `outcome`), and throw a domain exception if the follow-up is not found or already completed.
- [ ] Create `tracking/domain/event/FollowUpCompleted.java` (Domain Event)

### Application

- [ ] Create DTO `tracking/application/dto/CompleteFollowUpInput.java` (Requires `outcome`).
- [ ] Create `tracking/application/CompleteFollowUpUseCase.java`
  - Loads the `JobApplication`, verifies ownership via `AuthenticatedUser`.
  - Calls `completeFollowUp()`, saves the aggregate, and publishes the event.

### Web / UI

- [ ] Update `tracking/web/JobApplicationController.java` (or the specific `FollowUpController`)
  - `PATCH /applications/{appId}/follow-ups/{followUpId}/complete` (or `POST` with `_method=patch`) — handles the completion request.
- [ ] Update `templates/tracking/application-detail.html`
  - Add the UI for marking a follow-up as complete (e.g., an inline dropdown for the outcome + a "Complete" button, or a small modal dialog).
