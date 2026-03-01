# EPIC-TR01: Application CRUD

**Bounded Context:** Tracking (Core)
**Priority:** P0

---

## Feature SPEC

### Problem

Users need a central place to register job applications so they do not lose track of where they applied. Without this, the entire system has no data and the core value proposition of the tracker is lost.

### Business Rules

- A `JobApplication` requires at minimum: job title, company name, and status.
- Status defaults to `SAVED` on creation if not explicitly provided.
- A `JobApplication` belongs to exactly one User (strict data isolation enforced by `userId`).
- If provided, URLs (e.g., job posting link) and Emails (e.g., recruiter contact) must be in a valid format.
- If both are provided, the minimum salary must be less than or equal to the maximum salary.
- Duplicate applications (same job title + company for the same user) are allowed but must trigger a UI warning.
- Deleting a `JobApplication` cascades to all internal child entities within the aggregate (Interviews, FollowUps, Offers, JobSkills).
- Key lifecycle changes (creation, deletion, or description updates) publish domain events (e.g., `JobApplicationCreated`, `JobDescriptionUpdated`, `JobApplicationDeleted`) to enable asynchronous integration with other bounded contexts.

### Ubiquitous Language

| Term              | Definition                                                                                                               |
| ----------------- | ------------------------------------------------------------------------------------------------------------------------ |
| JobApplication    | The central Aggregate Root. A record of a user's interest or submission to a job posting, containing its full lifecycle. |
| ApplicationStatus | The current position of the application in the hiring pipeline, governed by defined transition rules.                    |
| WorkMode          | Categorization of the work environment (Remote, Hybrid, or On-site).                                                     |
| ContractType      | Categorization of the employment agreement (Full-time, Part-time, Contract, Internship).                                 |
| Recruiter         | The point of contact for the application (name, company, email).                                                         |

---

## Vertical Slices

| ID      | Slice                                                               | Dependencies              |
| ------- | ------------------------------------------------------------------- | ------------------------- |
| TR01-S1 | [Create a Job Application](TR01-S1-create-application.md)           | ID01-S1 (User must exist) |
| TR01-S2 | [View and Edit a Job Application](TR01-S2-view-edit-application.md) | TR01-S1                   |
| TR01-S3 | [Delete a Job Application](TR01-S3-delete-application.md)           | TR01-S1                   |
