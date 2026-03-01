# Aggregates

## Identity Context

**User** (aggregate root)
- Fields: id (UUID), email, passwordHash, displayName, createdAt, updatedAt
- Invariants:
  - Email must be unique across all Users
  - Email must be valid format
  - Password is always stored as BCrypt hash
  - Display name is required (max 100 chars)
- Repository: `UserRepository` — save, findByEmail, existsByEmail

## Tracking Context

**JobApplication** (aggregate root)
- Fields: id (UUID), userId, title, companyName, location, workMode, postingUrl, postingDescription, appliedAt, source, status, salaryMin, salaryMax, salaryCurrency, contractType, recruiterName, recruiterCompany, recruiterEmail, notes, createdAt, updatedAt
- Recruiter data is stored as flat fields rather than a separate value object. The three fields (name, company, email) are always read/written together with the JobApplication and do not have standalone behavior or validation beyond individual field formats.
- Child entities (managed through aggregate root):
  - **Interview** — scheduledAt, type, format, interviewers, preparationNotes, outcome
  - **FollowUp** — dueDate, note, completed, outcome
  - **Offer** — offerDate, salaryValue, salaryCurrency, contractType, status, notes
- Invariants:
  - Title and company are required
  - Status defaults to SAVED
  - All data scoped by userId
  - Salary min <= salary max when both provided
  - URL and email format validation when provided
  - Pipeline transitions enforced by PipelineService
  - One Offer per application (UNIQUE constraint)
  - All write operations on children go through aggregate root
- Repository: `JobApplicationRepository` — save, findById, findAllByUserId, delete

## Skills Context

**Skill** (aggregate root)
- Fields: id (UUID), name (canonical, unique), category, createdAt, updatedAt
- Child value objects:
  - **SkillAlias** — alias (unique, maps to one Skill)
- Invariants:
  - Canonical name must be unique
  - Alias must be unique (one alias maps to one skill)
  - Every skill has a category
- Repository: `SkillRepository` — findByName, findByAlias, findAll, findByCategory

**JobSkill** (cross-context association)
- Fields: id (UUID), applicationId (UUID, from Tracking), skillId (UUID, from Skills)
- Owned by the Skills context — the `job_application_skills` table is managed by the Skills context's application layer
- Created when processing `JobDescriptionUpdated` / `JobApplicationCreated` events (auto-extraction) or when the user manually tags skills
- applicationId is a primitive UUID correlation identifier — no import of Tracking domain model
- Repository: `JobSkillRepository` — save, deleteByApplicationId, findByApplicationId
