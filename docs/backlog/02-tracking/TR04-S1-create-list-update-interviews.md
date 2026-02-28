# Slice TR04-S1: Create, List, and Update Interviews

**Epic:** [EPIC-TR04](EPIC-TR04-interviews.md)
**Priority:** P0
**Sprint:** 3

---

## 🎯 Value Delivered

An authenticated User can record interviews with all their details and track their outcomes. This helps the user prepare effectively for upcoming meetings and maintains a clear history of the pipeline progression without forcefully altering the application's overall status.

## 🔗 Dependencies

- **TR01-S1 & TR01-S2** — A `JobApplication` must exist to attach interviews, and the detail view is needed to list and interact with them.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce ownership.

---

## 📖 User Story

As an **authenticated User**, I want to **add, view, and update interviews associated with a job application**, so that **I can prepare effectively and track the outcome of each stage**.

---

## ✅ Acceptance Criteria

**Scenario 1: Successfully Schedule an Interview**

- The user navigates to an application's detail page and submits the interview form.
- The system validates that the scheduled date/time and the interview type (`HR`, `TECHNICAL`, `FINAL`) are provided.
- The new `Interview` is attached to the `JobApplication` aggregate with its outcome defaulting to `PENDING`.
- The application's overall `ApplicationStatus` remains completely unchanged (the user retains manual control over the pipeline).
- The UI updates to display the newly scheduled interview in the list.

**Scenario 2: Update Interview Outcome**

- The user selects a pending interview and updates its outcome to `PASSED` or `FAILED`.
- The system delegates the operation to the `JobApplication` aggregate, which locates the specific `Interview` and applies the change.
- The updated aggregate is persisted to the database.

**Scenario 3: Enforce Aggregate Root Rules and Ownership**

- The system strictly verifies that the parent `JobApplication` belongs to the currently authenticated user (`userId` check).
- The `Interview` is never saved or updated directly via an independent repository; all state changes pass through the `JobApplication.addInterview(...)` and `JobApplication.updateInterviewOutcome(...)` methods.

**Scenario 4: Domain Event Publication**

- Upon successfully persisting the aggregate with the new interview, the system publishes an `InterviewScheduled` domain event.
- Upon successfully updating the outcome of an existing interview, the system publishes an `InterviewOutcomeUpdated` domain event.

---

## 🛠️ Technical Tasks (Implementation)

### Database & Infra

- [ ] Create Flyway migration `V4__create_interviews_table.sql`
  - Table `core.interviews`.
  - PostgreSQL enum types: `interview_type`, `interview_format`, `interview_outcome`.
  - Columns per `DATA-SCHEMA-DETAIL.md`, FK to `job_applications` with cascade delete, indexes on the FK and scheduled date.
- [ ] Update `tracking/infrastructure/JpaJobApplicationEntity.java`
  - Add `@OneToMany` mapping for `JpaInterviewEntity` with `CascadeType.ALL` and `orphanRemoval = true`.
- [ ] Create `tracking/infrastructure/JpaInterviewEntity.java`
  - Map properties and handle the three enum mappings.

### Domain

- [ ] Create Enums: `tracking/domain/InterviewType.java`, `InterviewFormat.java`, `InterviewOutcome.java`.
- [ ] Create `tracking/domain/Interview.java` (Child Entity)
  - Factory method with validation (requires scheduled date and type; defaults outcome to `PENDING`).
- [ ] Update `tracking/domain/JobApplication.java` (Aggregate Root)
  - Add `addInterview(Interview interview)` method.
  - Add `updateInterviewOutcome(UUID interviewId, InterviewOutcome outcome)` method.
- [ ] Create Domain Events: `tracking/domain/event/InterviewScheduled.java` and `InterviewOutcomeUpdated.java`.

### Application

- [ ] Create DTOs `AddInterviewInput.java`, `UpdateInterviewInput.java`, and `InterviewOutput.java`.
- [ ] Create `tracking/application/AddInterviewUseCase.java`
  - Loads `JobApplication`, verifies ownership, calls `addInterview()`, saves aggregate, and publishes `InterviewScheduled`.
- [ ] Create `tracking/application/UpdateInterviewUseCase.java`
  - Loads `JobApplication`, verifies ownership, calls `updateInterviewOutcome()`, saves aggregate, and publishes `InterviewOutcomeUpdated`.
- [ ] Create `tracking/application/ListInterviewsUseCase.java`
  - Loads the `JobApplication` by ID and returns its parsed interviews as a DTO list.

### Web / UI

- [ ] Update `tracking/web/JobApplicationController.java` (or create an `InterviewController`)
  - `POST /applications/{id}/interviews` — handles creation.
  - `GET /applications/{id}/interviews` — handles listing (if not injected directly into the detail model).
  - `PUT /applications/{appId}/interviews/{interviewId}` (or `PATCH`) — handles outcome updates.
- [ ] Update `templates/tracking/application-detail.html`
  - Add the interview scheduling form (date, type, format, interviewers, notes).
  - Add the list view for existing interviews, including a UI mechanism (e.g., inline dropdown or buttons) to update the outcome of a pending interview.
