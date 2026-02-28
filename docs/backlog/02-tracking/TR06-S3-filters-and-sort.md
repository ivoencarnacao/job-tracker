# Slice TR06-S3: Date Range, Additional Filters & Sort

**Epic:** [EPIC-TR06](EPIC-TR06-search-and-filter.md)
**Priority:** P0/P2
**Sprint:** 4

---

## 🎯 Value Delivered

An authenticated User can narrow their job applications by date range, source, and work mode, and sort the results by various criteria. This provides complete control over the application dashboard, allowing users to perform complex queries (e.g., "Show me all Remote applications from LinkedIn sorted by last updated") effortlessly.

## 🔗 Dependencies

- **TR06-S2** — Extends the Specification pattern established for the status filter.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce strict data isolation.

---

## 📖 User Story

As an **authenticated User**, I want to **apply multiple filters (date range, source, work mode) and sort my job applications**, so that **I can organize, analyze, and review my application history effectively**.

---

## ✅ Acceptance Criteria

**Scenario 1: Complex Filtering Combination**

- The user selects a combination of filters, such as a specific date range (`dateFrom` to `dateTo`), sources (e.g., LinkedIn, Company Website), and work modes (e.g., `REMOTE`, `HYBRID`).
- The system dynamically chains these new specifications with any existing search terms or status filters.
- The UI displays only the applications that match all selected criteria (logical `AND` across different filter categories, logical `OR` within multi-selects like work mode).

**Scenario 2: Sorting Results**

- The user selects a sorting criteria from a dropdown or table header (e.g., application date, last updated, or company name).
- The user toggles between ascending (ASC) and descending (DESC) order.
- The system applies the `Sort` parameter to the database query, returning the filtered records in the exact requested order.

**Scenario 3: Strict Data Isolation (Invariant)**

- Regardless of the complexity of the filter combination or sorting rules, the query is always strictly scoped to the authenticated user's `userId`.
- The `hasUserId` specification remains the mandatory base predicate for all read operations.

---

## 🛠️ Technical Tasks (Implementation)

### Application

- [ ] Create `tracking/application/dto/ApplicationFilterCriteria.java` (DTO)
  - Encapsulate all filter fields: `searchTerm`, `statuses`, `dateFrom`, `dateTo`, `sources`, `workModes`, `sortBy`, `sortDirection`.
- [ ] Update `tracking/application/SearchApplicationsUseCase.java`
  - Refactor to accept the `ApplicationFilterCriteria` DTO instead of individual parameters.
  - Map the DTO fields to the corresponding Specifications and the Spring Data `Sort` object.

### Infrastructure

- [ ] Update `tracking/infrastructure/JobApplicationSpecifications.java`
  - Add `hasCreatedAfter(LocalDate date)` and `hasCreatedBefore(LocalDate date)`.
  - Add `hasSourceIn(Set<String> sources)`.
  - Add `hasWorkModeIn(Set<WorkMode> workModes)`.
- [ ] Implement Sort Mapping
  - Ensure the domain/application sort fields map correctly to the JPA entity fields (e.g., `?sort=appliedAt,desc`, `?sort=updatedAt,desc`, `?sort=companyName,asc`).

### Web / UI

- [ ] Update `tracking/web/JobApplicationController.java`
  - Bind the incoming request parameters to the `ApplicationFilterCriteria` DTO (e.g., using `@ModelAttribute` or individual `@RequestParam` mapped to the DTO).
  - Pass the DTO to the Use Case and inject it back into the view model to preserve the UI state.
- [ ] Update `templates/tracking/application-list.html`
  - Add UI controls: Date range pickers (From/To).
  - Add UI controls: Source multi-select dropdown and Work Mode multi-select dropdown.
  - Add UI controls: Sort selector (dropdown or clickable table headers).
