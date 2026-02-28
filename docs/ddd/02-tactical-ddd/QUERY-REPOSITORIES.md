# Read-Only Query Repositories (Dashboard Context)

## Repositories

| Repository | Queries | Source Tables |
|------------|---------|---------------|
| DashboardQueryRepository | countByUserId, countByUserIdGroupByStatus, countByUserIdAndCreatedAfter, responseRate, upcomingInterviews, pendingFollowUps, noResponseAlerts | job_applications, interviews, follow_ups |
| SkillInsightsQueryRepository | topSkillsByUserId, categoryBreakdown, skillFrequency | job_application_skills, skills |

## Pattern

These are infrastructure-level read-only repositories that query across Tracking and Skills tables. This is the Open Host Service pattern — Dashboard does not import domain models, it runs its own optimized queries.

The Dashboard context has no aggregates of its own. It only contains:
- **application/** — Query use cases and read-model DTOs
- **infrastructure/** — Read-only query implementations (JPA projections, native queries)
- **web/** — Controllers and view models
