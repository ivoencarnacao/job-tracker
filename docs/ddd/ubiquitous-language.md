# Ubiquitous Language / Glossary

## Language Convention

The PRD and user-facing UI use **Portuguese** terminology. The codebase uses **English** terminology. This glossary is the authoritative mapping between the two. When discussing the domain, prefer the English terms in code and technical discussions, and the Portuguese terms in user stories, UI labels, and product conversations.

> **Important:** The English word "Application" is ambiguous in this project (it can mean the Spring Boot application or a job application). In code, we use **`JobApplication`** to avoid confusion. In domain discussions, the unqualified word "application" always means a job application.

## Core Domain Terms

| Portuguese | English (Code) | Definition |
|---|---|---|
| Candidatura | JobApplication | A record of the user's interest in or submission to a specific job posting. It has a lifecycle modeled by the pipeline states. The central aggregate of the domain. |
| Estado da Candidatura | ApplicationStatus | The current position of a job application in the hiring pipeline. Transitions follow defined rules. |
| Pipeline | Pipeline | The ordered sequence of stages a job application passes through, from initial save to a terminal state (Offer accepted, Rejected, Ghosted, or Withdrawn). |

## Pipeline States

| Portuguese | Enum (Code) | Definition |
|---|---|---|
| Guardada | SAVED | Wishlist state: the user has saved a job posting but has not yet applied. |
| Candidatada | APPLIED | The user has submitted their application to the employer. |
| Em Analise | UNDER_REVIEW | The employer has acknowledged receipt or the application is being evaluated. |
| Entrevista RH | HR_INTERVIEW | The application has progressed to an HR/screening interview. |
| Entrevista Tecnica | TECHNICAL_INTERVIEW | The application has progressed to a technical interview. |
| Challenge / Case Study | CHALLENGE | The employer has assigned a technical challenge or case study. |
| Entrevista Final | FINAL_INTERVIEW | The application has progressed to a final-round interview. |
| Proposta | OFFER | The employer has extended a formal job offer. |
| Rejeitada | REJECTED | The employer has explicitly rejected the application. Terminal state. |
| Sem Resposta / Ghosted | GHOSTED | No response received for an extended period (default: 21 days). Can be set manually or auto-suggested by the system. Terminal state (reversible if employer responds). |
| Fechada pelo Utilizador | WITHDRAWN | The user has voluntarily closed/abandoned this application. Terminal state. |

## Supporting Domain Terms

| Portuguese | English (Code) | Definition |
|---|---|---|
| Utilizador | User | A registered person using the system to track their job search. Each user has private, isolated data. |
| Entrevista | Interview | A scheduled meeting between the user and employer representatives, associated with a specific job application. Has a type (HR, Technical, Final), format (Online, On-site), and outcome (Pending, Passed, Failed). |
| FollowUp | FollowUp | An internal reminder the user sets to follow up on a job application. Has a due date, completion status, and optional outcome note. Not an email integration in MVP. |
| Proposta (detalhe) | Offer | A formal job offer received from an employer, with details like salary, contract type, benefits, and a status (Received, Accepted, Declined, Expired). Linked to a JobApplication. |
| Skill | Skill | A professional competency or technology mentioned in job postings (e.g., "Java", "Spring Boot", "Docker"). Belongs to a category. Has a canonical name for normalization. |
| Categoria de Skill | SkillCategory | A grouping for skills: Language, Framework, Database, Cloud, DevOps, Testing, Architecture, Tool, Methodology, Soft Skill. |
| Competencia da Vaga | JobSkill | The association between a job application and a skill, representing that the job posting requires or mentions that skill. |
| Vaga | JobPosting | The original job listing/advertisement. In this domain, the job posting data is captured as part of the JobApplication (title, company, location, description, link). It is not a separate entity in MVP. |
| Empresa | Company | The organization offering the job. Stored as attributes of a JobApplication (company name). Not a separate entity in MVP. |
| Fonte | Source | Where the user found the job posting (e.g., LinkedIn, CVWarehouse, company website, referral). A simple text/enum field on JobApplication. |
| Modo de Trabalho | WorkMode | Whether the job is Remote, Hybrid, or On-site. |
| Tipo de Contrato | ContractType | Employment arrangement: Full-time, Part-time, Contract, Internship. |
| Recrutador | Recruiter | The contact person for a job application (name, company, email). Note: the recruiter's company may differ from the hiring company (e.g., Randstad recruiting for another firm). |
| Dashboard | Dashboard | The overview screen showing aggregate metrics: total applications, counts by status, response rate, pending follow-ups, upcoming interviews. A read-only projection of domain data. |
| Tendencia | Trend | A statistical insight derived from skills data: frequency of skills across job postings, changes over time, top categories. |
| Taxa de Resposta | ResponseRate | The percentage of applications that received any employer response (positive or negative) versus total applications sent. |
| Alerta de Sem Resposta | NoResponseAlert | A dashboard warning shown when an application has remained in Applied or Under Review for more than 14 days without any status change. Distinct from auto-ghosting (21 days) — this is an earlier heads-up to the user. |
| Ghosting Automatico | AutoGhosting | A domain rule that suggests marking an application as GHOSTED after 21 days without any status change from APPLIED or UNDER_REVIEW. |

## Domain Rules

1. A **JobApplication** starts as **Saved** or **Applied**.
2. A **Saved** application transitions to **Applied** when the user submits it.
3. **Ghosted** can be **auto-suggested** when an application remains in Applied or Under Review for more than 21 days without update.
4. **Terminal states** are: Rejected, Ghosted, Withdrawn, and Offer (when accepted or declined).
5. **Ghosted** is reversible: if the employer responds, the application can transition to a new active state.
6. An **Interview** always belongs to exactly one **JobApplication**.
7. A **FollowUp** always belongs to exactly one **JobApplication**.
8. An **Offer** always belongs to exactly one **JobApplication**.
9. **Skills** are extracted from the job posting description or added manually.
10. **Skill normalization** ensures variants map to one canonical skill (e.g., "PostgreSQL", "Postgres", "psql" all map to the skill "PostgreSQL").
11. Each **User** owns all their **JobApplications** -- strict data isolation between users.
12. A **NoResponseAlert** is shown on the dashboard when an application has been in Applied or Under Review for more than 14 days without update. This is a warning, not a status change. The auto-ghosting suggestion (21 days) is a separate, later trigger.
