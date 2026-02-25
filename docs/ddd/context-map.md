# Context Map

## Diagram

```
+---------------------------------------------------------------------+
|                      Job Tracker (Monolith)                          |
|                                                                      |
|  +------------------------+                                          |
|  |                        |                                          |
|  |   Identity Context     |                                          |
|  |                        |                                          |
|  |   [SUPPORTING]         |                                          |
|  |                        |                                          |
|  |  - User                |                                          |
|  |  - RegistrationService |                                          |
|  |                        |                                          |
|  +----+--------+----------+                                          |
|       |        |                                                     |
|       | CF     | CF                                                  |
|       v        v                                                     |
|  +----+-------------+         +------------------------+            |
|  |                   |  U/D   |                        |            |
|  | Tracking Context  +------->|   Skills Context       |            |
|  |                   |  (CF)  |                        |            |
|  | [CORE]            |        |   [CORE]               |            |
|  |                   |        |                        |            |
|  | - JobApplication  |        |   - Skill              |            |
|  | - Interview       |        |   - SkillCategory      |            |
|  | - FollowUp        |        |   - SkillExtractor     |            |
|  | - Offer           |        |   - TrendAnalyzer      |            |
|  | - PipelineService |        |                        |            |
|  | - GhostingPolicy  |        |                        |            |
|  |                   |        |                        |            |
|  +--------+----------+        +----------+-------------+            |
|           |                              |                           |
|           | U/D (OHS)                    | U/D (OHS)                 |
|           v                              v                           |
|  +-------------------------------------------------------+          |
|  |                                                       |          |
|  |              Dashboard Context                        |          |
|  |                                                       |          |
|  |              [SUPPORTING]                             |          |
|  |                                                       |          |
|  |  - DashboardService (read model)                     |          |
|  |  - InsightsService (read model)                      |          |
|  |                                                       |          |
|  +-------------------------------------------------------+          |
|                                                                      |
+---------------------------------------------------------------------+

Legend:
  U/D  = Upstream / Downstream
  CF   = Conformist (downstream conforms to upstream's model)
  OHS  = Open Host Service (upstream exposes a clean query interface)
  ---> = data flows from upstream to downstream (arrow points to the consumer/dependent)
```

---

## Context Relationships

### Identity (upstream) --> Tracking (downstream): Conformist

The Tracking context references `userId` from the Identity context to scope all job application data per user. The Tracking context conforms to the Identity context's model -- it uses the userId as-is without requiring Identity to adapt.

**In practice (MVP monolith):** The Tracking context receives the authenticated userId from Spring Security's SecurityContext and uses it to filter all queries and associate new entities.

### Identity (upstream) --> Skills (downstream): Conformist

Same pattern as above. Skills data (JobSkill associations and trend queries) are scoped per user.

### Tracking (upstream) --> Skills (downstream): Conformist

The Skills context needs access to job application data (specifically the job description text and the application ID) to extract and associate skills. The Skills context conforms to the Tracking context's model -- it reads JobApplication data as-is.

**In practice (MVP monolith):** The Skills context receives the job description as a String parameter and the application ID when called. It does not import Tracking's aggregate classes directly. Instead, the application layer passes the needed data.

### Tracking (upstream) --> Dashboard (downstream): Open Host Service

The Dashboard context reads Tracking data to compute metrics. The Tracking context provides a query-friendly interface (repository methods, read-only projections) that the Dashboard can consume.

**In practice (MVP monolith):** DashboardService calls Tracking repository methods (count by status, find by date range, etc.) through well-defined read-only interfaces. No direct aggregate mutation.

### Skills (upstream) --> Dashboard (downstream): Open Host Service

The Dashboard context reads Skills data to present trend insights. The Skills context exposes query interfaces that the Dashboard consumes.

**In practice (MVP monolith):** InsightsService calls Skills repository/service methods to get top skills, category breakdowns, and temporal trends.

---

## Shared Kernel: None

There is no shared kernel between contexts in this design. Each context owns its terms completely. The only shared elements are primitive IDs (`userId`, `applicationId`) used as correlation identifiers, which are primitive types, not shared domain objects.

## Anti-Corruption Layer: Not Needed in MVP

Since all contexts are in the same monolith and under the same team's control, ACLs are unnecessary. If the Skills context were to integrate with an external skills taxonomy API in the future, an ACL would be appropriate at that boundary.

---

## Future Evolution

When the MVP proves successful, the bounded contexts can evolve:

1. **Skills context** could become a separate module with its own database schema, especially if skill extraction becomes more sophisticated (NLP, external APIs).
2. **Dashboard context** could adopt CQRS with materialized views for performance as dataset sizes grow.
3. **Identity context** could evolve to support OAuth2/OIDC when external identity providers are needed.
4. **A Notifications context** could emerge if email/calendar integrations are added (post-MVP Phase 2).
