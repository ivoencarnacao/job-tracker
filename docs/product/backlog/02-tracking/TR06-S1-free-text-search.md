# Slice TR06-S1: Free-Text Search

**Epic:** [EPIC-TR06](EPIC-TR06-search-and-filter.md)
**Priority:** P0
**Vertical Slice:** VS-06

---

## 🎯 Value Delivered

An authenticated User can quickly find specific job applications by typing keywords related to the company or job title. This vastly improves the usability of the dashboard as the user's list of tracked applications grows over time.

## 🔗 Dependencies

- **TR01-S1** — Applications must exist in the database to be searched.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce strict data isolation.

---

## 📖 User Story

As an **authenticated User**, I want to **search my job applications using free-text keywords**, so that **I can instantly locate specific opportunities by company name or job title**.

---

## ✅ Acceptance Criteria

**Scenario 1: Case-Insensitive Search**

- The user enters a search term in the search bar and submits the query.
- The system performs a case-insensitive search (`ILIKE` or `LOWER`) against both the `title` and `company_name` fields.
- The UI displays only the applications that partially or fully match the search term.

**Scenario 2: Strict Data Isolation**

- The search query is strictly scoped to the authenticated user's `userId`.
- It is impossible for a user to retrieve applications belonging to another user, regardless of the search term used.

**Scenario 3: Empty or Cleared Search**

- If the user submits an empty search query or clears the search bar, the system returns the default unfiltered list of the user's applications.
- If a search yields no results, the UI displays a friendly "No applications found" message rather than an empty table.

**Scenario 4: Pure Read Operation**

- The search operation does not mutate any aggregate state and does not trigger any domain events.

---

## 🛠️ Technical Tasks (Implementation)

### Domain

- [ ] Update `tracking/domain/JobApplicationRepository.java`
  - Add the method signature: `List<JobApplication> searchByUserIdAndKeyword(UUID userId, String keyword)`.

### Infrastructure

- [ ] Update `tracking/infrastructure/JpaJobApplicationRepository.java`
  - Implement the search method using a Spring Data JPA `@Query`.
  - Example logic: `WHERE user_id = :userId AND (LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(company_name) LIKE LOWER(CONCAT('%', :keyword, '%')))`.

### Application

- [ ] Create `tracking/application/SearchApplicationsUseCase.java` (or extend `ListJobApplicationsUseCase` to accept an optional query parameter).
  - Extracts the `userId` from the `AuthenticatedUser`.
  - Calls the repository search method and maps the results to output DTOs.

### Web / UI

- [ ] Update `tracking/web/JobApplicationController.java`
  - Modify the existing list endpoint (e.g., `GET /applications`) to accept an optional `?q=` query parameter.
  - Inject the search query back into the view model to keep the search bar populated after submission.
- [ ] Update `templates/tracking/application-list.html`
  - Add a search bar form (input field and submit button) above the application list.
  - Add a conditional "empty state" block when the search results are zero.
