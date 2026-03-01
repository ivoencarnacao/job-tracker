# Application Events

Events tracked server-side to feed business metrics and hypothesis validation.

| Event | Trigger | Data | Feeds Metric |
|-------|---------|------|-------------|
| `user.registered` | Successful registration | userId, timestamp | Conversion rate, total users |
| `user.session_started` | Login or active session | userId, timestamp | WAU, retention |
| `application.created` | Application saved | userId, timestamp, hasDescription | Apps/user, activation rate, time to first value |
| `application.status_updated` | Pipeline status change | userId, appId, fromStatus, toStatus | Status update rate, known final state, ghosted rate |
| `follow_up.created` | Follow-up added | userId, appId | Follow-up usage |
| `follow_up.completed` | Follow-up marked complete | userId, appId, outcome | H4 validation |
| `interview.created` | Interview recorded | userId, appId | Upcoming interviews |
| `offer.created` | Offer recorded | userId, appId | Offer tracking |
| `skill.extracted` | Auto-extraction completed | appId, skillCount | H2 validation |
| `page.insights_viewed` | Insights page loaded | userId | Insights visits, H3 validation |
