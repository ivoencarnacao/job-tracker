# Subdomains

## Overview

The Job Tracker domain decomposes into four subdomains. Classification follows DDD conventions:

- **Core:** Provides competitive advantage; this is what we build and invest in most. Custom-built, carefully designed.
- **Supporting:** Necessary for the core to function but not a differentiator. Still custom-built but with less design investment.
- **Generic:** Solved problems with off-the-shelf solutions. We do not invest design effort here.

---

## 1. Application Tracking (Core)

**Classification:** Core

**Justification:** This is the heart of the product. The hiring pipeline model with its domain-specific states (HR Interview, Technical Interview, Challenge, Ghosted with auto-suggestion) is the primary reason users choose this tool. Getting this right -- with the correct state machine, smooth transitions, and meaningful constraints -- is what makes the product valuable. A generic task tracker cannot replicate this domain knowledge.

**Responsibilities:**

- Job application lifecycle management (create, update, archive, delete)
- Pipeline state machine with valid transitions
- Association with interviews, follow-ups, and offers
- Storage of job posting metadata (title, company, location, link, source, description, salary range, recruiter contact)
- Work mode and contract type classification
- Auto-ghosting rule (suggest GHOSTED after 21 days of inactivity)
- Search and filtering across applications

**Key Entities:** JobApplication (aggregate root), ApplicationStatus (value object/enum), Interview, FollowUp, Offer

**PRD Coverage:** RF1 (CRUD candidaturas), RF3 (Follow-ups), RF4 (Entrevistas), RF5 (Propostas), RF7 (Pesquisa e filtros)

---

## 2. Skills and Trends Analysis (Core)

**Classification:** Core

**Justification:** This is the product's **key differentiator** as stated in the PRD. Competitors offer tracking; few offer market insight derived from the user's own saved job postings. The skill extraction, normalization, categorization, and trend computation contain domain-specific logic that directly drives the product's unique value proposition.

**Responsibilities:**

- Skill catalog management (canonical names, aliases, categories)
- Skill extraction from job posting descriptions (keyword matching in MVP)
- Skill normalization (mapping variants to canonical names)
- Association of skills to job applications (JobSkill)
- Frequency and trend computation (top skills, category breakdown, temporal trends over 30/60 days)
- Skill category taxonomy (Language, Framework, Database, Cloud, DevOps, Testing, Architecture, Tool, Methodology)

**Key Entities:** Skill (aggregate root), SkillCategory (enum/value object), JobSkill

**PRD Coverage:** RF6 (Skills / Tendencias)

---

## 3. Reporting and Dashboard (Supporting)

**Classification:** Supporting

**Justification:** The dashboard provides essential visibility into the user's job search, but it is a **read model** -- it aggregates and presents data owned by the core subdomains. It does not introduce new domain concepts or business rules. The value comes from the data it displays, not from the dashboard logic itself.

**Responsibilities:**

- Aggregate metrics computation (total applications, counts by status, applications this week)
- Response rate calculation
- Pending follow-ups listing
- Upcoming interviews listing
- "No response" alerts (applications in Applied/Under Review for 14+ days without update)
- Skills insights presentation (top skills, category breakdown, trends)

**Key Concepts:** Dashboard (read model), ResponseRate (derived metric), Trend (derived insight)

**PRD Coverage:** RF2 (Dashboard)

---

## 4. Identity and Access Management (Supporting)

**Classification:** Supporting

**Justification:** The application is multi-user, requiring user registration, authentication, and strict data isolation between users. While Spring Security handles the authentication mechanics, the User entity is a domain concept that owns all job application data. Data isolation rules are domain-specific, not generic framework behavior.

**Responsibilities:**

- User registration and account management
- Authentication (login/logout)
- Session management
- Password encoding
- Data ownership -- ensuring each user only accesses their own data
- Basic authorization

**Key Entities:** User (aggregate root)

**PRD Coverage:** Acceptance criteria #1 (Criar conta e entrar), NFR Security and Privacy

---

## Summary

| Subdomain | Type | Build vs. Buy | Design Investment |
|---|---|---|---|
| Application Tracking | Core | Build (custom) | High |
| Skills and Trends Analysis | Core | Build (custom) | High |
| Reporting and Dashboard | Supporting | Build (queries) | Medium |
| Identity and Access | Supporting | Build (Spring Security + custom User) | Medium |

## Subdomain Interactions

```
Identity and Access  ──owns/secures────> Application Tracking
Identity and Access  ──owns/secures────> Skills and Trends
Application Tracking ──provides data──> Reporting and Dashboard
Skills and Trends    ──provides data──> Reporting and Dashboard
Application Tracking ──owns skill link─> Skills and Trends
```

## Note on Scope

This subdomain decomposition models the **full domain**, not just MVP P0 features. Skill extraction (P1), auto-ghosting suggestions (P1), and temporal trends (P2) are modeled here because they are part of the domain regardless of delivery priority. See `docs/MVP-SCOPE.md` for the phased delivery plan.
