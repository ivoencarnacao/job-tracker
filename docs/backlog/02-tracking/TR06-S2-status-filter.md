# Slice TR06-S2: Status Filter

**Epic:** [EPIC-TR06](EPIC-TR06-search-and-filter.md)
**Priority:** P0
**Sprint:** 3

---

## 🎯 Value Delivered

An authenticated User can filter their job applications by specific pipeline statuses (e.g., viewing only "Applied" and "Under Review"). This allows the user to focus on actionable items and specific stages of their job hunt without being overwhelmed by the entire history.

## 🔗 Dependencies

- **TR06-S1** — Extends the base search functionality and application list view.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce strict data isolation.

---

## 📖 User Story

As an **authenticated User**, I want to **filter my job applications by one or multiple statuses**, so that **I can focus on specific stages of my hiring pipeline**.

---

## ✅ Acceptance Criteria

**Scenario 1: Multi-Status Filtering**

- The user selects one or multiple statuses from the filter UI (e.g., using checkboxes or clickable pills).
- The system executes a query using an `IN` clause for the selected statuses.
- The UI updates to display only the applications that match any of the selected statuses.

**Scenario 2: Dynamic Query Combination (Search + Filter)**

- The user types a keyword in the search bar AND selects specific statuses.
- The system dynamically chains the query criteria (e.g., `(title LIKE %keyword% OR company LIKE %keyword%) AND status IN (selected_statuses)`).
- The results accurately reflect the intersection of both filters.

**Scenario 3: Strict Data Isolation (Mandatory Specification)**

- The filter query is always strictly scoped to the authenticated user's `userId`.
- The `hasUserId` specification acts as a mandatory base predicate that cannot be bypassed, ensuring no cross-user data leakage occurs.

**Scenario 4: Clear Filter State**

- If the user deselects all statuses (or clears the filter), the system defaults to showing applications across all statuses (respecting any active text search).

---

## 🛠️ Technical Tasks (Implementation)

### Infrastructure

- [ ] Update `tracking/domain/JobApplicationRepository.java`
  - Extend `JpaSpecificationExecutor<JpaJobApplicationEntity>` (if using Spring Data JPA directly at the infrastructure level) or define a custom domain interface for dynamic queries.
- [ ] Create `tracking/infrastructure/JobApplicationSpecifications.java`
  - Implement reusable specification methods:
    - `hasUserId(UUID userId)`
    - `hasSearchTerm(String searchTerm)` (migrated from TR06-S1)
    - `hasStatusIn(Set<ApplicationStatus> statuses)`

### Application

- [ ] Update `tracking/application/SearchApplicationsUseCase.java` (or equivalent query handler)
  - Accept a new parameter: `Set<ApplicationStatus> statuses`.
  - Dynamically chain the specifications using `Specification.where(hasUserId).and(hasSearchTerm).and(hasStatusIn)` before executing the repository query.

### Web / UI

- [ ] Update `tracking/web/JobApplicationController.java`
  - Modify the list endpoint to accept an optional `?status=` query parameter (capable of accepting multiple values, e.g., `?status=APPLIED&status=UNDER_REVIEW`).
  - Inject the selected statuses back into the view model to keep the UI controls in the correct state.
- [ ] Update `templates/tracking/application-list.html`
  - Add a status filter UI component (e.g., a row of toggleable status pills or a multi-select dropdown form).
