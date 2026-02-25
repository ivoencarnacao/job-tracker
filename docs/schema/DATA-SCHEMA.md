# Data Schema (Conceptual)

This document describes the data model at a conceptual level — entities, attributes, relationships, and enum definitions. For column-level database specifications, see [DATA-SCHEMA-DETAIL.md](DATA-SCHEMA-DETAIL.md).

---

## Entities

### User

Represents a registered person using the system.

| Attribute | Type | Required | Notes |
|-----------|------|----------|-------|
| Email | String | Yes | Unique identifier for login |
| Password | String (hashed) | Yes | Stored as BCrypt hash |
| Display name | String | Yes | Shown in the UI |

---

### JobApplication

The central entity. Represents a user's interest in or submission to a specific job posting.

| Attribute | Type | Required | Notes |
|-----------|------|----------|-------|
| Job title | String | Yes | |
| Company name | String | Yes | The hiring company |
| Location | String | No | City or region |
| Work mode | Enum (WorkMode) | No | |
| Posting URL | String | No | Link to the original job listing |
| Posting description | Text | No | Full description, used for skill extraction |
| Application date | Date | No | When the user actually applied |
| Status | Enum (ApplicationStatus) | Yes | Current pipeline position. Default: SAVED |
| Source | String | No | Where the posting was found |
| Salary min | Number | No | |
| Salary max | Number | No | |
| Salary currency | String | No | ISO 4217 code |
| Contract type | Enum (ContractType) | No | |
| Recruiter name | String | No | |
| Recruiter company | String | No | May differ from hiring company |
| Recruiter email | String | No | |
| Notes | Text | No | Free-form user notes |

---

### Interview

A scheduled meeting between the user and employer, linked to one application.

| Attribute | Type | Required | Notes |
|-----------|------|----------|-------|
| Scheduled at | DateTime | Yes | Date and time of the interview |
| Type | Enum (InterviewType) | Yes | |
| Format | Enum (InterviewFormat) | No | |
| Interviewers | String | No | Names of interviewer(s) |
| Preparation notes | Text | No | |
| Outcome | Enum (InterviewOutcome) | Yes | Default: PENDING |

---

### FollowUp

An internal reminder set by the user on a specific application.

| Attribute | Type | Required | Notes |
|-----------|------|----------|-------|
| Due date | Date | Yes | When the follow-up should happen |
| Note | Text | No | Context for the follow-up |
| Completed | Boolean | Yes | Default: false |
| Outcome | Enum (FollowUpOutcome) | No | Set when completed |

---

### Offer

A formal job offer received from an employer, linked to one application.

| Attribute | Type | Required | Notes |
|-----------|------|----------|-------|
| Offer date | Date | Yes | When the offer was received |
| Salary value | Number | No | |
| Salary currency | String | No | ISO 4217 code |
| Contract type | Enum (ContractType) | No | |
| Status | Enum (OfferStatus) | Yes | Default: RECEIVED |
| Notes | Text | No | Benefits, conditions, doubts |

---

### Skill

A professional competency or technology. Has a canonical name for normalization.

| Attribute | Type | Required | Notes |
|-----------|------|----------|-------|
| Name | String | Yes | Canonical name (e.g., "PostgreSQL") |
| Category | Enum (SkillCategory) | Yes | |
| Aliases | List of Strings | No | Alternative names (e.g., "Postgres", "psql") |

---

### JobSkill (Association)

Links a job application to a skill, meaning the job posting mentions or requires that skill.

| Attribute | Type | Required | Notes |
|-----------|------|----------|-------|
| Job application | Reference | Yes | |
| Skill | Reference | Yes | |

---

## Enum Definitions

### ApplicationStatus

| Value | Description |
|-------|-------------|
| SAVED | Wishlist — not yet applied |
| APPLIED | Application submitted |
| UNDER_REVIEW | Employer acknowledged receipt |
| HR_INTERVIEW | Progressed to HR screening |
| TECHNICAL_INTERVIEW | Progressed to technical interview |
| CHALLENGE | Technical challenge or case study assigned |
| FINAL_INTERVIEW | Final interview round |
| OFFER | Employer extended an offer |
| REJECTED | Employer explicitly rejected (terminal) |
| GHOSTED | No response for 21+ days (terminal, reversible) |
| WITHDRAWN | User voluntarily closed application (terminal) |

### InterviewType

HR, Technical, Final

### InterviewFormat

Online, On-site

### InterviewOutcome

Pending, Passed, Failed

### OfferStatus

Received, Accepted, Declined, Expired

### WorkMode

Remote, Hybrid, On-site

### ContractType

Full-time, Part-time, Contract, Internship

### SkillCategory

Language, Framework, Database, Cloud, DevOps, Testing, Architecture, Tool, Methodology, Soft Skill

### FollowUpOutcome

Responded, No Response

---

## Relationships

```
User 1 ──── N JobApplication
                │
                ├── 1 ──── N Interview
                │
                ├── 1 ──── N FollowUp
                │
                ├── 1 ──── 1 Offer        (one offer per application in MVP)
                │
                └── N ──── M Skill         (via JobSkill association)
```

- A **User** owns many **JobApplications** (strict data isolation between users)
- A **JobApplication** has many **Interviews** (one per stage)
- A **JobApplication** has many **FollowUps**
- A **JobApplication** has at most one **Offer** (in MVP)
- A **JobApplication** has many **Skills** (via the JobSkill join table)
- A **Skill** can be associated with many **JobApplications**

---

## Entity-Relationship Diagram

```
┌──────────┐       ┌──────────────────┐       ┌─────────────┐
│   User   │1────N│  JobApplication   │N────M│    Skill     │
│          │       │                  │       │             │
│ email    │       │ title            │       │ name        │
│ password │       │ company_name     │       │ category    │
│ display  │       │ status           │       │ aliases     │
│  _name   │       │ location         │       └─────────────┘
└──────────┘       │ work_mode        │
                   │ posting_url      │
                   │ posting_desc     │
                   │ applied_at       │
                   │ source           │
                   │ salary_min/max   │
                   │ contract_type    │
                   │ recruiter_*      │
                   │ notes            │
                   └───┬───┬───┬──────┘
                       │   │   │
                    1──N 1──N 1──1
                       │   │   │
               ┌───────┘   │   └────────┐
               │           │            │
        ┌──────┴─────┐ ┌──┴──────┐ ┌───┴────┐
        │  Interview  │ │ FollowUp│ │ Offer  │
        │             │ │         │ │        │
        │ scheduled_at│ │ due_date│ │ date   │
        │ type        │ │ note    │ │ salary │
        │ format      │ │completed│ │ status │
        │ interviewers│ │ outcome │ │ notes  │
        │ prep_notes  │ └─────────┘ └────────┘
        │ outcome     │
        └─────────────┘
```
