# Edge Cases

## Application Management

### Duplicate Applications
- **Scenario:** User creates two applications for the same company and same job title.
- **Handling:** Allow it. Users may re-apply to the same role months later, or the same title may exist in different locations. Show a warning ("You already have an application for [title] at [company]") but do not block creation.

### Empty Dashboard
- **Scenario:** New user with zero applications views the dashboard.
- **Handling:** Show an empty state with a clear call-to-action ("Add your first application to get started") instead of zeros and blank charts.

### Bulk Deletion
- **Scenario:** User wants to delete many old applications at once.
- **Handling:** MVP does not support bulk actions. User deletes one at a time. Confirmation dialog required before each deletion.

## Pipeline State Transitions

### Invalid Transitions
- **Scenario:** User tries to move an application from "Rejected" back to "HR Interview."
- **Handling:** Terminal states (Rejected, Withdrawn) are final. The system should prevent backward transitions from terminal states and explain why. Exception: Ghosted is reversible — if the employer responds, the user can move it to any active state.

### Skipping States
- **Scenario:** User moves directly from "Saved" to "Technical Interview" (skipping Applied and HR Interview).
- **Handling:** Allow it. Not all hiring processes follow the same sequence. The pipeline states are not strictly sequential — users have flexibility.

### Auto-Ghosting Conflict
- **Scenario:** System suggests marking an application as Ghosted (21 days without update), but the user is on vacation and simply hasn't updated.
- **Handling:** Auto-ghosting is a suggestion only, never automatic. The system shows a notification ("No update for 21+ days — mark as ghosted?") but the user decides. The suggestion can be dismissed.

### Status Change After Offer
- **Scenario:** User has an application in "Offer" status but the company rescinds the offer.
- **Handling:** Allow transition from Offer to Rejected or Withdrawn. These are valid real-world scenarios.

## Interviews and Follow-ups

### Interview Without Status Change
- **Scenario:** User records an interview but forgets to update the application status.
- **Handling:** Creating an interview does not auto-change the application status. The dashboard could show a hint ("Application still marked as Applied, but has an interview recorded — update status?").

### Past-Date Follow-up
- **Scenario:** User sets a follow-up reminder for a date that has already passed.
- **Handling:** Allow it. The user may be retroactively recording a follow-up they already did. It immediately appears as overdue in the pending follow-ups list.

### Multiple Offers
- **Scenario:** User receives a revised offer from the same company (e.g., salary negotiation).
- **Handling:** MVP supports one offer per application. The user should update the existing offer record rather than creating a new one. Notes field can capture the negotiation history.

## Skill Extraction

### No Recognizable Skills
- **Scenario:** User pastes a job description that contains no skills from the dictionary (e.g., a non-tech role or very generic posting).
- **Handling:** Show a message ("No skills detected — you can add skills manually") and present the manual tagging interface.

### Very Long Descriptions
- **Scenario:** User pastes an extremely long text (10,000+ characters) as a job description.
- **Handling:** Impose a reasonable limit (e.g., 50,000 characters). Truncate with a warning if exceeded. Skill extraction processes the full stored text.

### Duplicate Skills on Same Application
- **Scenario:** The same skill appears multiple times in a description (e.g., "Java" mentioned 5 times).
- **Handling:** Count as one skill per application. Frequency across applications is what matters, not mentions within a single description.

### Skills in Non-English Descriptions
- **Scenario:** Job description is in Portuguese or another language, but tech skills are in English.
- **Handling:** The skill dictionary matches keywords regardless of surrounding language. Most tech skills (Java, Docker, AWS) are language-agnostic. This is a natural advantage of keyword matching.

## Data and Privacy

### Cascade on Application Deletion
- **Scenario:** User deletes an application that has linked interviews, follow-ups, offers, and skills.
- **Handling:** Cascade delete all child records. Show a confirmation dialog listing what will be deleted ("This application has 2 interviews, 1 follow-up, and 1 offer. Delete everything?").

### Account Deletion
- **Scenario:** User wants to delete their account entirely.
- **Handling:** Delete all user data (applications, interviews, follow-ups, offers, skill associations). This is irreversible. Require password confirmation and a clear warning.

## Concurrency

### Multiple Tabs
- **Scenario:** User has the same application open in two browser tabs and edits in both.
- **Handling:** Last save wins in MVP. Optimistic locking (version column) can detect conflicts and show a message ("This record was modified since you opened it — reload and try again").
