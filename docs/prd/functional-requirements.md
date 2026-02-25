# Functional Requirements

## RF1 — Application CRUD

Create, edit, archive, and delete job applications.

### Fields

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| Job title | Text (max 255) | Yes | |
| Company | Text (max 255) | Yes | |
| Location | Text (max 255) | No | City/region |
| Work mode | Enum | No | Remote, Hybrid, On-site |
| Job posting URL | URL (max 2048) | No | |
| Job description | Long text | No | Used for skill extraction |
| Application date | Date | No | When the user applied |
| Status | Enum | Yes | Default: Saved. See pipeline states below. |
| Source | Text (max 100) | No | LinkedIn, company website, referral, etc. |
| Notes | Long text | No | Free-form |
| Salary range (min/max) | Number | No | |
| Salary currency | Text (3 chars) | No | ISO 4217 (EUR, USD, etc.) |
| Contract type | Enum | No | Full-time, Part-time, Contract, Internship |
| Recruiter name | Text (max 255) | No | |
| Recruiter company | Text (max 255) | No | May differ from the hiring company |
| Recruiter email | Email (max 255) | No | |

### Pipeline States

Saved → Applied → Under Review → HR Interview → Technical Interview → Challenge → Final Interview → Offer

Terminal states: Rejected, Ghosted, Withdrawn

> **Ghosted** can be set manually or auto-suggested after 21 days without status change from Applied or Under Review.

### Validation Rules

- Job title and company are mandatory
- Status defaults to "Saved" on creation
- URL must be a valid URL format if provided
- Salary min must be less than or equal to salary max if both provided
- Email must be a valid email format if provided

---

## RF2 — Dashboard

Display key metrics in a summary view.

### Metrics

| Metric | Description |
|--------|-------------|
| Total applications | Count of all non-archived applications |
| Count by status | Number of applications in each pipeline state |
| This week's applications | Applications created in the last 7 days |
| Response rate | (Applications with any employer response) / (Total applications sent) |
| Upcoming interviews | Interviews scheduled in the next 7 days |
| Pending follow-ups | Follow-ups due or overdue |
| No response alerts | Applications in Applied/Under Review for more than X days (default: 14) |

### Behavior

- Dashboard refreshes on page load (server-rendered)
- All metrics scoped to the authenticated user

---

## RF3 — Follow-ups

Create and manage internal reminders per application.

### Fields

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| Due date | Date | Yes | When to follow up |
| Note | Text | No | Context for the follow-up |
| Completed | Boolean | Yes | Default: false |
| Outcome | Enum | No | Responded, No Response (set when completed) |

### Behavior

- A follow-up belongs to exactly one application
- Multiple follow-ups can exist per application
- Dashboard shows overdue and upcoming follow-ups
- Marking as completed prompts for outcome

---

## RF4 — Interviews

Record and track interviews linked to applications.

### Fields

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| Date/time | DateTime | Yes | |
| Type | Enum | Yes | HR, Technical, Final |
| Format | Enum | No | Online, On-site |
| Interviewer(s) | Text (max 500) | No | Names of interviewers |
| Preparation notes | Long text | No | |
| Outcome | Enum | Yes | Default: Pending. Options: Pending, Passed, Failed |

### Behavior

- An interview belongs to exactly one application
- Multiple interviews can exist per application (one per stage)
- Creating an interview does not automatically change the application status

---

## RF5 — Offers

Record job offers received from employers.

### Fields

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| Offer date | Date | Yes | |
| Salary value | Number | No | |
| Salary currency | Text (3 chars) | No | ISO 4217 |
| Contract type | Enum | No | Full-time, Part-time, Contract, Internship |
| Status | Enum | Yes | Received, Accepted, Declined, Expired |
| Notes | Long text | No | Benefits, conditions, doubts |

### Behavior

- An offer belongs to exactly one application
- One offer per application in MVP
- Recording an offer does not automatically change the application status to "Offer" (user controls the pipeline)

---

## RF6 — Skills and Trends

Extract and analyze hard skills from saved job postings.

### Skill Input

- **Automatic:** When a job description is saved, the system scans for known skills using keyword matching against a built-in dictionary
- **Manual fallback:** Users can manually tag skills on any application

### Skill Dictionary (Initial)

A predefined dictionary of common tech skills grouped by category:

| Category | Examples |
|----------|----------|
| Language | Java, JavaScript, TypeScript, Python, C#, Go, Kotlin, PHP, Ruby, SQL |
| Framework | Spring Boot, React, Angular, Vue, Node.js, Django, .NET, Express |
| Database | PostgreSQL, MySQL, MongoDB, Redis, Oracle, SQL Server, DynamoDB |
| Cloud | AWS, Azure, GCP, Heroku, DigitalOcean |
| DevOps | Docker, Kubernetes, Jenkins, GitLab CI, GitHub Actions, Terraform, Ansible |
| Testing | JUnit, Mockito, Selenium, Cypress, Jest, pytest |
| Architecture | Microservices, REST, GraphQL, Event-Driven, CQRS, DDD |
| Tool | Git, Jira, Confluence, Figma, Postman, SonarQube |
| Methodology | Scrum, Kanban, Agile, TDD, BDD, CI/CD |
| Soft Skill | Teamwork, Communication, Leadership, Problem-solving |

### Normalization

Variants of the same skill map to a single canonical name (e.g., "PostgreSQL", "Postgres", "psql" → "PostgreSQL").

### Insights

| Insight | Description |
|---------|-------------|
| Top skills | Skills sorted by frequency across all user's postings |
| Category breakdown | Skills grouped by category with counts |
| Skill frequency | Percentage of postings mentioning each skill |
| Temporal trend (Should) | Frequency changes over the last 30/60 days |

---

## RF7 — Search and Filters

Find and organize applications quickly.

### Search

- Free-text search by company name and job title
- Results update as user types (or on submit)

### Filters

| Filter | Type | Options |
|--------|------|---------|
| Status | Multi-select | All pipeline states |
| Date range | Date picker | Application date from/to |
| Source | Multi-select | All sources used |
| Work mode | Multi-select | Remote, Hybrid, On-site |

### Sorting

| Sort option | Direction |
|-------------|-----------|
| Application date | Newest first / Oldest first |
| Last updated | Most recent first |
| Company name | A–Z / Z–A |
