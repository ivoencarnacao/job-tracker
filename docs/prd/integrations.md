# Integrations

## MVP — No External Integrations

The MVP intentionally avoids external API integrations to reduce complexity and shipping time. All data is entered manually by the user.

### What This Means

- No LinkedIn/Indeed/Glassdoor API integration
- No Gmail or email integration for auto-detecting responses
- No Google Calendar or Outlook sync for interviews
- No OAuth/social login (email + password only)
- No external analytics or tracking services (privacy-first approach)

### Internal Technical Integrations

These are framework-level integrations, not external services:

| Integration | Purpose |
|-------------|---------|
| Spring Security | Authentication, session management, CSRF protection |
| Flyway | Database schema versioning and migrations |
| Docker Compose | Local development environment (PostgreSQL, pgAdmin) |
| Testcontainers | Integration testing with real PostgreSQL instances |

## Future Phases

### Phase 2 (Post-MVP)

| Integration | Purpose | Notes |
|-------------|---------|-------|
| Gmail API | Detect employer responses automatically | Requires OAuth consent flow |
| Google Calendar | Sync interview dates bidirectionally | Requires OAuth consent flow |
| URL scraping (light) | Auto-fill job details from a posting URL | Fragile, site-dependent |

### Phase 3

| Integration | Purpose | Notes |
|-------------|---------|-------|
| LinkedIn API | Import job postings and application history | Restricted API access, may need partnership |
| OAuth providers | Social login (Google, GitHub) | Simplifies registration |
| Export APIs | CSV/PDF export of application data | User data portability |
