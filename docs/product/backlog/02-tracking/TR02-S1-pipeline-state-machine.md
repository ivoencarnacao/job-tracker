# Slice TR02-S1: Pipeline State Machine and Status Update

**Epic:** [EPIC-TR02](EPIC-TR02-pipeline-management.md)
**Priority:** P0
**Vertical Slice:** VS-03

---

## 🎯 Value Delivered

An authenticated User can change the status of their job application with domain-enforced transition rules. This ensures the hiring pipeline remains logically consistent, blocking invalid state jumps while providing clear feedback.

## 🔗 Dependencies

- **TR01-S1** — A `JobApplication` must exist to have its status updated.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce ownership before allowing status changes.

---

## 📖 User Story

As an **authenticated User**, I want to **update the status of my job application**, so that **I can accurately track its progress through the hiring pipeline**.

---

## ✅ Acceptance Criteria

**Scenario 1: Successful and Flexible Status Update**

- The user views an application in a non-terminal state and selects a new valid status.
- The system validates the transition, updates the `JobApplication`'s status, and refreshes the `updated_at` timestamp.
- The UI displays the updated status and a success message.

**Scenario 2: Terminal States Protection**

- The application is currently in a terminal state (`REJECTED` or `WITHDRAWN`).
- The system strictly blocks any further status transitions.
- The UI prevents the user from attempting a transition (e.g., by disabling the status dropdown or hiding the update button) and displays an error if a direct API call bypasses the UI.

**Scenario 3: Reversible States**

- The application is currently in the `GHOSTED` state.
- The system allows the user to transition it back to any active state (e.g., `HR_INTERVIEW` or `TECHNICAL_INTERVIEW`) if the employer resumes contact.

**Scenario 4: Valid Options Filtering (UI UX)**

- When the user opens the status update dropdown in the detail or edit view.
- The dropdown only displays the valid target states based on the application's current state (provided by the `PipelineService`).

**Scenario 5: Domain Event Publication** *(Deferred to P1)*

> Event infrastructure will be introduced with the Skills context (VS-09/VS-10). See [architecture/FUTURE.md](../../../architecture/FUTURE.md).

- Upon successfully persisting the new status, the system publishes the `ApplicationStatusChanged` domain event, including the `applicationId`, `previousStatus`, and `newStatus`.

---

## 🛠️ Technical Tasks (Implementation)

### Domain

- [ ] Create `tracking/domain/PipelineService.java` (Domain Service)
  - Method `validateTransition(currentStatus, newStatus)` — throws a domain exception if the transition is invalid.
  - Method `getValidTransitions(currentStatus)` — returns a set of allowed target states.
  - Implement rules: Block transitions FROM `REJECTED`/`WITHDRAWN`; allow FROM `GHOSTED` to any active state; allow flexible transitions between non-terminal states.
- [ ] *(P1)* Create `tracking/domain/event/ApplicationStatusChanged.java`
  - Plain Java record representing the event: `(UUID applicationId, String previousStatus, String newStatus)`.

### Application

- [ ] Create DTO `tracking/application/dto/UpdateStatusInput.java`
- [ ] Create `tracking/application/UpdatePipelineStatusUseCase.java`
  - Loads the `JobApplication` and verifies ownership via `AuthenticatedUser`.
  - Delegates the transition validation to `PipelineService`.
  - Updates the aggregate and saves it.
  - *(P1)* Publishes the `ApplicationStatusChanged` domain event.

### Web / UI

- [ ] Update `tracking/web/JobApplicationController.java`
  - Add `PATCH /applications/{id}/status` endpoint (or `POST` with `_method=patch`) to handle the status update submission.
  - Inject valid transition options into the model for the view.
- [ ] Update `templates/tracking/application-detail.html` & `application-form.html`
  - Status change dropdown must dynamically render only the valid target states (using `getValidTransitions`).
  - Add specific error display blocks for invalid transition attempts.
