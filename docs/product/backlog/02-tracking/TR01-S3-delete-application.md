# Slice TR01-S3: Delete a Job Application

**Epic:** [EPIC-TR01](EPIC-TR01-application-crud.md)
**Priority:** P0
**Vertical Slice:** VS-03

---

## 🎯 Value Delivered

An authenticated User can permanently remove a job application they no longer wish to track. This ensures the user retains full control over their data and can keep their dashboard relevant and clutter-free, with full awareness of the cascading impact of the deletion.

## 🔗 Dependencies

- **TR01-S1 & TR01-S2** — A `JobApplication` must exist to be deleted.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce ownership.

---

## 📖 User Story

As an **authenticated User**, I want to **delete a job application and its related data**, so that **I can remove applications I am no longer pursuing or entered by mistake**.

---

## ✅ Acceptance Criteria

**Scenario 1: Ownership Enforcement**

- The user requests to delete a specific application.
- The system verifies that the `JobApplication` belongs to the currently authenticated user (`userId` check).
- If the user is not the owner, the system denies the action (`AccessDeniedException` or `NotFoundException`), preventing unauthorized deletions.

**Scenario 2: Deletion Confirmation and Cascade Awareness**

- Before executing the deletion, the UI presents a mandatory confirmation prompt (modal or dedicated page).
- The prompt clearly warns the user that the action is irreversible and lists the cascading impact (e.g., "This will also delete X interviews, Y follow-ups, and Z offers associated with this application").
- The deletion is only processed if the user explicitly confirms the prompt.

**Scenario 3: Successful Cascade Deletion**

- Upon confirmation, the system removes the `JobApplication` aggregate from the database.
- All associated child entities (Interviews, FollowUps, Offers, JobSkills) are permanently deleted alongside the root aggregate (handled efficiently via database cascade mechanisms to maintain referential integrity).
- The UI redirects the user to the application list with a success message.

**Scenario 4: Domain Event Publication on Deletion**

- After the aggregate is successfully deleted from the database, the system publishes a `JobApplicationDeleted` domain event.
- This ensures downstream contexts (e.g., Search, AI Parsing) can remove the associated data from their indexes or materialized views without tight coupling.

---

## 🛠️ Technical Tasks (Implementation)

### Application

- [ ] Create `tracking/application/DeleteJobApplicationUseCase.java`
  - Loads the application to verify ownership against the `AuthenticatedUser`.
  - Initiates the deletion through the repository.
  - Publishes the `JobApplicationDeleted` domain event post-deletion.

### Web / UI

- [ ] Update `tracking/web/JobApplicationController.java`
  - `GET /applications/{id}/delete` (Optional) — renders a dedicated confirmation page summarizing the child entities that will be lost.
  - `DELETE /applications/{id}` (or `POST` with `_method=delete`) — processes the actual deletion.
- [ ] Create or update `templates/tracking/application-delete-confirm.html` (or add a modal to the detail view)
  - UI to display the warning and require explicit confirmation before submitting the delete request.
