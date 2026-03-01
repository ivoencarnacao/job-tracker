# Cross-Context Communication

## Communication Map

| From     | To           | Mechanism                             | Data Passed                            |
| -------- | ------------ | ------------------------------------- | -------------------------------------- |
| Identity | All contexts | userId from SecurityContext           | UUID (primitive)                       |
| Tracking | Skills       | `JobApplicationCreated` domain event  | UUID applicationId, UUID userId, boolean hasDescription |
| Tracking | Skills       | `JobDescriptionUpdated` domain event  | UUID applicationId, String description |
| Tracking | Dashboard    | Domain events + Open Host Service     | Events for metrics; read-only queries for aggregates |
| Skills   | Dashboard    | Open Host Service (read-only queries) | Query results (DTOs)                   |
| Skills   | Tracking     | JobSkill write (Skills owns `job_application_skills`) | Skills context creates/deletes JobSkill associations using applicationId (UUID) from events or manual tagging |

## Rules

- No context imports another context's domain model
- Correlation via primitive IDs only (userId, applicationId)
- Dashboard context has no aggregates — read-only DTOs/view models only
- Shared Kernel: none (each context owns its terms completely)
