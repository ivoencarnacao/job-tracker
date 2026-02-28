# EPIC-TR06: Search, Filter & Sort

**Bounded Context:** Tracking (Core)
**Priority:** P0

---

## Feature SPEC

### Problem

As users accumulate job applications over time, they need a fast and reliable way to find specific records and organize their dashboard. Without robust search, filtering, and sorting capabilities, the application list quickly becomes unmanageable and loses its value as a tracking tool.

### Business Rules

- **Strict Data Isolation:** All search, filter, and sort queries must be strictly scoped to the authenticated `userId`. A user can never query or retrieve another user's applications.
- **Search:** Free-text search must query by both company name and job title, and must be case-insensitive.
- **Filtering:** Users can filter by status (multi-select), application date range (from/to), source (multi-select), and work mode (multi-select).
- **Sorting:** Results can be sorted by application date (ascending/descending), last updated (descending - default), or company name (ascending/descending).
- **Combinability:** Search, filter, and sort parameters can be combined freely in a single query.
- **Read-Only Operation:** These operations do not mutate the aggregate state; they are purely read operations (queries) and do not publish domain events.

### Ubiquitous Language

No new terms are introduced in this Epic. It relies entirely on the existing properties of the `JobApplication` aggregate and its child entities.

---

## Vertical Slices

| ID      | Slice                                                                | Dependencies |
| ------- | -------------------------------------------------------------------- | ------------ |
| TR06-S1 | [Free-Text Search](TR06-S1-free-text-search.md)                      | TR01-S1      |
| TR06-S2 | [Status Filter](TR06-S2-status-filter.md)                            | TR06-S1      |
| TR06-S3 | [Date Range, Additional Filters & Sort](TR06-S3-filters-and-sort.md) | TR06-S2      |
