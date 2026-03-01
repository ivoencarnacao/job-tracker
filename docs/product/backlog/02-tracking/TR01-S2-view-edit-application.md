# Slice TR01-S2: View and Edit a Job Application

**Epic:** [EPIC-TR01](EPIC-TR01-application-crud.md)
**Priority:** P0
**Vertical Slice:** VS-02

---

## 🎯 Value Delivered

An authenticated User can view the full details of an existing job application and update any of its fields. This ensures the user's tracker remains accurate as they progress through the hiring pipeline.

## 🔗 Dependencies

- **TR01-S1** — A `JobApplication` must exist to be viewed or edited.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce ownership.

---

## 📖 User Story

As an **authenticated User**, I want to **view the full details of a specific job application and update its information**, so that **I can keep my job hunt records accurate and up to date**.

---

## ✅ Acceptance Criteria

**Scenario 1: View Application Details (Ownership Enforced)**

- The user requests to view the details of a specific application.
- The system retrieves the `JobApplication` and verifies that its `userId` matches the currently authenticated user.
- If the user is the owner, the UI displays the read-only detail view (`application-detail.html`) with all fields.
- If the user is not the owner (or the ID doesn't exist), the system throws a `NotFoundException` or `AccessDeniedException` (preventing IDOR - Insecure Direct Object Reference vulnerabilities).

**Scenario 2: Successful Application Update**

- The user navigates to the edit form, which is pre-populated with the current application data.
- The user updates the fields and submits the form.
- The system enforces the exact same domain invariants as the creation process (Title/Company required, valid URL/Email formats, Salary Min <= Salary Max).
- The `JobApplication` aggregate is successfully updated and persisted in the database.
- The UI redirects the user back to the detail view with a success message.

**Scenario 3: Domain Event Publication on Update** *(Deferred to P1)*

> Event infrastructure will be introduced with the Skills context (VS-09/VS-10). See [architecture/FUTURE.md](../../../architecture/FUTURE.md).

- The user updates the text within the job description field.
- Upon successful persistence, the system publishes the `JobDescriptionUpdated` domain event, allowing downstream contexts to re-parse or re-index the updated description.
- If the description was not changed, no event is published.

---

## 🛠️ Technical Tasks (Implementation)

### Domain

- [ ] Update `tracking/domain/JobApplication.java`
  - Add an `update(...)` method to the aggregate root that applies changes and re-evaluates all invariant validations internally.

### Application

- [ ] Create DTO `tracking/application/dto/UpdateJobApplicationInput.java`
- [ ] Create `tracking/application/GetJobApplicationUseCase.java`
  - Loads the application by ID via the repository.
  - Strictly verifies that the aggregate's `userId` matches the current `AuthenticatedUser`.
- [ ] Create `tracking/application/UpdateJobApplicationUseCase.java`
  - Loads the application, verifies ownership, applies the update via the aggregate's `update` method.
  - Saves the updated aggregate.
  - *(P1)* Evaluates if the description changed and publishes `JobDescriptionUpdated` if true.

### Web / UI

- [ ] Update `tracking/web/JobApplicationController.java`
  - `GET /applications/{id}` — renders the detail view.
  - `GET /applications/{id}/edit` — renders the edit form, pre-populated.
  - `PUT /applications/{id}` (or `POST` with `_method=put`) — processes the update.
- [ ] Create `templates/tracking/application-detail.html`
  - Read-only detail view displaying all application fields.
- [ ] Update `templates/tracking/application-form.html`
  - Ensure the template is reusable for both "Create" and "Edit" modes, dynamically adjusting the form action and pre-filling inputs.
