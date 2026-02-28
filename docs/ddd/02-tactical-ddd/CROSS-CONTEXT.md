# Cross-Context Communication

## Communication Map

| From     | To           | Mechanism                             | Data Passed                            |
| -------- | ------------ | ------------------------------------- | -------------------------------------- |
| Identity | All contexts | userId from SecurityContext           | UUID (primitive)                       |
| Tracking | Skills       | `JobDescriptionUpdated` domain event  | UUID applicationId, String description |
| Tracking | Dashboard    | Open Host Service (read-only queries) | Query results (DTOs)                   |
| Skills   | Dashboard    | Open Host Service (read-only queries) | Query results (DTOs)                   |

## Rules

- No context imports another context's domain model
- Correlation via primitive IDs only (userId, applicationId)
- Dashboard context has no aggregates — read-only DTOs/view models only
- Shared Kernel: none (each context owns its terms completely)
