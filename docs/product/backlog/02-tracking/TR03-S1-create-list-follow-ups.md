# Slice TR03-S1: Create and List Follow-Ups

**Epic:** [EPIC-TR03](EPIC-TR03-follow-ups.md)
**Priority:** P0
**Vertical Slice:** VS-04

---

## 🎯 Value Delivered

An authenticated User can set follow-up reminders on an application and see them listed. This prevents applications from going stale and helps the user maintain momentum in their job search.

## 🔗 Dependencies

- **TR01-S1 & TR01-S2** — A `JobApplication` must exist to attach follow-ups, and the detail view is needed to list them.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce ownership via the parent aggregate.

---

## 📖 User Story

As an **authenticated User**, I want to **add follow-up reminders to my job applications and view a list of them**, so that **I never miss an opportunity to engage with recruiters**.

---

## ✅ Acceptance Criteria

**Scenario 1: Successfully Add a Follow-Up**

- The user navigates to an application's detail page and submits the follow-up form with a valid due date (and optional note).
- The system validates the input (due date is required).
- The new `FollowUp` is attached to the `JobApplication` aggregate with `completed` defaulting to `false`.
- The system persists the aggregate, saving the child entity correctly via cascade.
- The UI updates to display the newly added follow-up in the list.

**Scenario 2: Enforce Aggregate Root Rules and Ownership**

- The system strictly verifies that the parent `JobApplication` belongs to the currently authenticated user (`userId` check).
- If the user tries to bypass the UI to add a follow-up to an application they do not own, the system throws an `AccessDeniedException` or `NotFoundException`.
- The `FollowUp` is never saved directly through an independent repository; it must pass through the `JobApplication.addFollowUp(...)` method.

**Scenario 3: List Associated Follow-Ups**

- When the user views the application detail page, the system retrieves and displays all associated follow-ups ordered by due date.
- Follow-ups with a due date in the past and `completed = false` are visually highlighted as "overdue" in the UI.

**Scenario 4: Domain Event Publication** *(Deferred to P1)*

> Event infrastructure will be introduced with the Skills context (VS-09/VS-10). See [architecture/FUTURE.md](../../../architecture/FUTURE.md).

- Upon successfully persisting the aggregate with the new follow-up, the system publishes a `FollowUpScheduled` domain event.

---

## 🛠️ Technical Tasks (Implementation)

### Database & Infra

- [ ] Create Flyway migration `V3__create_follow_ups_table.sql`
  - Table `core.follow_ups`.
  - PostgreSQL enum type: `follow_up_outcome` (values: `RESPONDED`, `NO_RESPONSE`).
  - Columns per `DATA-SCHEMA-DETAIL.md`, FK to `job_applications` with cascade delete, indexes on the FK and due date.
- [ ] Update `tracking/infrastructure/JpaJobApplicationEntity.java`
  - Add `@OneToMany` mapping for `JpaFollowUpEntity` with `CascadeType.ALL` and `orphanRemoval = true`.
- [ ] Create `tracking/infrastructure/JpaFollowUpEntity.java`
  - Map properties, handle `FollowUpOutcome` enum mapping.

### Domain

- [ ] Create `tracking/domain/FollowUpOutcome.java` (Enum: `RESPONDED`, `NO_RESPONSE`)
- [ ] Create `tracking/domain/FollowUp.java` (Child Entity)
  - Factory method, requires due date validation.
- [ ] Update `tracking/domain/JobApplication.java` (Aggregate Root)
  - Add `addFollowUp(FollowUp followUp)` method to manage its internal collection and encapsulate the logic.
- [ ] *(P1)* Create `tracking/domain/event/FollowUpScheduled.java` (Domain Event)

### Application

- [ ] Create DTO `tracking/application/dto/AddFollowUpInput.java`
- [ ] Create `tracking/application/AddFollowUpUseCase.java`
  - Loads `JobApplication`, verifies ownership, calls `addFollowUp()`, and saves the aggregate. *(P1: publishes the event.)*
- [ ] Create `tracking/application/ListFollowUpsUseCase.java`
  - Loads the `JobApplication` by ID and returns its parsed follow-ups as a DTO list.

### Web / UI

- [ ] Update `tracking/web/JobApplicationController.java` (or create a new `FollowUpController` nested under the application route)
  - `POST /applications/{id}/follow-ups` — handles creation.
  - `GET /applications/{id}/follow-ups` — returns the list (or include it in the main detail model).
- [ ] Update `templates/tracking/application-detail.html`
  - Add the follow-up form (due date, optional note).
  - Add the list view for existing follow-ups, including logic to flag overdue items visually.
