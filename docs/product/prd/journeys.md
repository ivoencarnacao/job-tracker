# User Journeys

> For a high-level cross-context flow, see [Global User Journey](../backlog/JOURNEY.md).

## Journey 1 — First-Time Onboarding

**Trigger:** User discovers the app and wants to start tracking.

| Step | User Action | System Response |
|------|-------------|-----------------|
| 1 | Visits landing page | Displays value proposition and sign-up CTA |
| 2 | Clicks "Sign Up" | Shows registration form (email, password, display name) |
| 3 | Submits registration | Creates account, logs user in, redirects to empty dashboard |
| 4 | Sees empty dashboard | Displays a prompt: "Add your first application to get started" |
| 5 | Clicks "New Application" | Opens application form |
| 6 | Fills minimum fields (title, company, status) and saves | Creates application, redirects to dashboard showing first card |

**Alternative path:** User tries to register with an existing email → system shows error, suggests login.

---

## Journey 2 — Add Application

**Trigger:** User has a new job posting to track.

| Step | User Action | System Response |
|------|-------------|-----------------|
| 1 | Clicks "New Application" | Opens application form |
| 2 | Fills required fields: job title, company, status | Validates input |
| 3 | (Optional) Adds link, description, location, source, salary range | Accepts optional fields |
| 4 | (Optional) Pastes job description text | Stores description for skill extraction |
| 5 | Saves | Creates application, updates dashboard metrics, triggers skill extraction if description was provided |

**Alternative path:** User leaves required fields empty → form shows inline validation errors, does not submit.

---

## Journey 3 — Update Pipeline Status

**Trigger:** User receives a response from an employer.

| Step | User Action | System Response |
|------|-------------|-----------------|
| 1 | Opens an existing application | Displays full application details |
| 2 | Changes status (e.g., Applied → HR Interview) | Validates transition, updates status |
| 3 | (Optional) Adds an interview record with date, type, and notes | Saves interview linked to application |
| 4 | (Optional) Sets a follow-up reminder | Creates follow-up with due date |
| 5 | Saves | Updates application, reflects changes on dashboard |

**Alternative path:** User tries an invalid status transition → system prevents it and explains why.

---

## Journey 4 — Manage Follow-ups

**Trigger:** Dashboard shows pending follow-ups or a follow-up due date arrives.

| Step | User Action | System Response |
|------|-------------|-----------------|
| 1 | Sees "Pending Follow-ups" section on dashboard | Lists applications with due/overdue follow-ups |
| 2 | Clicks on an application with a pending follow-up | Opens application detail |
| 3 | Marks follow-up as completed | Updates follow-up status |
| 4 | Records outcome (responded / no response) | Saves outcome note |
| 5 | (Optional) If no response for extended period, marks as Ghosted | Updates application status to Ghosted |

**Alternative path:** User decides not to follow up → can reschedule the follow-up to a later date or dismiss it.

---

## Journey 5 — View Skill Trends

**Trigger:** User wants to understand what the market demands.

| Step | User Action | System Response |
|------|-------------|-----------------|
| 1 | Opens "Insights" section | Displays skill analytics dashboard |
| 2 | Views top skills ranking | Shows skills sorted by frequency across saved postings |
| 3 | Views category breakdown | Groups skills by category (Languages, Frameworks, Databases, Cloud, DevOps, Testing, Architecture, Tools, Methodologies, Soft Skills) |
| 4 | (Optional) Filters by time period (last 30/60 days) | Recalculates frequencies for selected period |
| 5 | Uses insights to adjust CV or study plan | (No system action — user-driven outcome) |

**Alternative path:** User has fewer than 5 applications with descriptions → system shows message explaining that more data is needed for meaningful insights.
