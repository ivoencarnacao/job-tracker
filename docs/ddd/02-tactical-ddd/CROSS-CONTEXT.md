# Cross-Context Communication

## Communication Map

| From     | To           | Mechanism                             | Data Passed                            |
| -------- | ------------ | ------------------------------------- | -------------------------------------- |
| Identity | All contexts | userId from SecurityContext           | UUID (primitive)                       |
| Tracking | Skills       | `JobApplicationCreated` domain event **(P1)** | UUID applicationId, UUID userId, boolean hasDescription |
| Tracking | Skills       | `JobDescriptionUpdated` domain event **(P1)** | UUID applicationId, String description |
| Tracking | Dashboard    | **MVP:** Open Host Service (read-only queries). **P1:** Domain events for cache invalidation | MVP: direct read-only queries for metrics/aggregates. P1: events for real-time updates |
| Skills   | Dashboard    | Open Host Service (read-only queries) | Query results (DTOs)                   |
| Skills   | Tracking     | JobSkill write (Skills owns `job_application_skills`) | Skills context creates/deletes JobSkill associations using applicationId (UUID) from events or manual tagging |

## Rules

- No context imports another context's domain model
- Correlation via primitive IDs only (userId, applicationId)
- Dashboard context has no aggregates — read-only DTOs/view models only
- Shared Kernel: none (each context owns its terms completely)
