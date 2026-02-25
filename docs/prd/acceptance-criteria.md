# Acceptance Criteria

The MVP is considered ready when a user can complete the following scenarios.

---

## AC-01: Registration and Login

**Given** a visitor on the landing page,
**When** they complete the registration form with valid email, password, and display name,
**Then** an account is created, the user is logged in, and they are redirected to the dashboard.

**Given** a registered user,
**When** they enter valid credentials on the login page,
**Then** they are authenticated and redirected to the dashboard.

---

## AC-02: Register Applications with Status

**Given** a logged-in user,
**When** they click "New Application" and fill in at least the job title, company, and status,
**Then** the application is saved and appears in their application list and dashboard metrics.

**Given** a logged-in user creating an application,
**When** they leave the job title or company empty and submit,
**Then** the form shows validation errors and does not save.

---

## AC-03: Update the Pipeline

**Given** a logged-in user with an existing application in "Applied" status,
**When** they change the status to "HR Interview" and save,
**Then** the status is updated, the dashboard metrics reflect the change, and the last-updated timestamp is refreshed.

**Given** a logged-in user with an application in "Rejected" status,
**When** they attempt to change the status to an active state,
**Then** the system prevents the transition and shows an explanation that Rejected is a terminal state.

**Given** a logged-in user with an application in "Ghosted" status,
**When** they change the status to an active state (employer responded),
**Then** the transition is allowed because Ghosted is reversible.

---

## AC-04: Create and Manage Follow-ups

**Given** a logged-in user viewing an application,
**When** they create a follow-up with a due date,
**Then** the follow-up is saved and appears in the dashboard's "Pending Follow-ups" section.

**Given** a logged-in user with a pending follow-up,
**When** they mark it as completed and select an outcome (Responded / No Response),
**Then** the follow-up is marked as done and no longer appears in the pending list.

---

## AC-05: Record Interviews and Offers

**Given** a logged-in user viewing an application,
**When** they add an interview with date, type (HR/Technical/Final), and outcome (Pending),
**Then** the interview is saved and linked to the application.

**Given** a logged-in user viewing an application,
**When** they record an offer with date, status (Received), and optional salary/notes,
**Then** the offer is saved and linked to the application.

---

## AC-06: View Dashboard with Metrics

**Given** a logged-in user with 10+ applications in various statuses,
**When** they visit the dashboard,
**Then** they see: total application count, count by status, applications this week, response rate, upcoming interviews, pending follow-ups, and no-response alerts.

**Given** a logged-in user with zero applications,
**When** they visit the dashboard,
**Then** they see an empty state with a prompt to add their first application.

---

## AC-07: View Skill Insights

**Given** a logged-in user with 5+ applications that include job descriptions,
**When** they visit the Insights section,
**Then** they see: top skills ranked by frequency, skills grouped by category, and frequency percentages.

**Given** a logged-in user with fewer than 5 applications with descriptions,
**When** they visit the Insights section,
**Then** they see a message explaining that more data is needed for meaningful insights.
