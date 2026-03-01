# Metric Definitions

Precise definitions for every metric in MVP-SCOPE and GTM-STRATEGY.

## Product Metrics (MVP-SCOPE)

| Metric | Definition | Formula | Target |
|--------|-----------|---------|--------|
| Applications per user (week 1) | Applications created within 7 days of registration | COUNT(application.created WHERE timestamp < user.registered + 7d) per user | 10+ |
| Weekly active users (WAU) | Users with 2+ sessions in a calendar week | Users WHERE COUNT(DISTINCT session weeks) ≥ 2 | 2+ sessions/week |
| Status update rate | Applications with at least one status change | COUNT(apps with status_updated) / COUNT(total apps) | > 50% |
| Follow-up usage | Users who created at least one follow-up | COUNT(DISTINCT users with follow_up.created) / COUNT(total users) | > 30% |
| Insights page visits | Users who visited /insights at least once | COUNT(DISTINCT users with page.insights_viewed) / COUNT(total users) | > 25% |
| Known final state | Applications in a terminal state | COUNT(apps in OFFER, REJECTED, GHOSTED, WITHDRAWN) / COUNT(total apps) | > 60% |
| Time to first value | Registration to first dashboard view with 1+ application | timestamp(first dashboard load with apps > 0) - timestamp(user.registered) | < 3 minutes |

## Acquisition Metrics (GTM-STRATEGY)

| Metric | Definition | Formula | Target |
|--------|-----------|---------|--------|
| Conversion rate | Landing page visitors who register | COUNT(user.registered) / COUNT(landing page views) | > 5% |
| Activation rate | Registered users who create 5+ applications | COUNT(users with 5+ apps) / COUNT(total users) | > 30% |
| Week-1 retention | Activated users who return in week 2 | COUNT(users with 2+ sessions in week 2) / COUNT(activated users) | > 40% |
| Referral rate | Users who shared the app | Track (no target yet) | — |
