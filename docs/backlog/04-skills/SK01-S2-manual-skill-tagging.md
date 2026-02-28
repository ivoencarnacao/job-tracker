# Slice SK01-S2: Manual Skill Tagging on Applications

**Epic:** [EPIC-SK01](EPIC-SK01-skill-catalog.md)
**Priority:** P1
**Sprint:** 5

---

## 🎯 Value Delivered

An authenticated User can manually add and remove skills on any job application. This enables accurate skill tracking and personalized insights even before automatic extraction mechanisms are implemented.

## 🔗 Dependencies

- **SK01-S1** — The skill catalog must exist to provide the available skills for tagging.
- **TR01-S1** — Job applications must exist to associate skills with them.

---

## 📖 User Story

As an **authenticated User**, I want to **manually tag my job applications with relevant skills**, so that **I can track which competencies are required for the roles I am applying for**.

---

## ✅ Acceptance Criteria

**Scenario 1: Tagging an Application with a Skill**

- The user navigates to an application's detail page and selects a skill from a searchable dropdown.
- The system associates the skill with the application, ensuring the operation is idempotent (no duplicate tags are created if the skill is already attached).
- The UI updates to display the newly tagged skill as a chip.

**Scenario 2: Removing a Skill Tag**

- The user clicks the remove button on an existing skill chip.
- The system deletes the association between the application and the skill.

**Scenario 3: Invariant Enforcement and Ownership**

- The database strictly enforces a maximum of one entry per (application, skill) pair via a composite primary key.
- Before adding, removing, or listing skills, the system strictly verifies ownership via the parent job application's `userId`.

---

## 🛠️ Technical Tasks (Implementation)

### Database & Infra

- [ ] Create Flyway migration `V8__create_job_application_skills_table.sql`.
  - Define a composite Primary Key using `job_application_id` and `skill_id`.
  - Add Foreign Keys to both tables and an index on `skill_id` for fast lookups.
- [ ] Create infrastructure JPA entity for the join table and its corresponding repository.

### Domain

- [ ] Create `skills/domain/JobSkill.java`.
  - Association entity managed via a domain repository.
- [ ] Create `skills/domain/JobSkillRepository.java`.
  - Interface methods: `save`, `delete`, `findByApplicationId`, `existsByApplicationIdAndSkillId`.

### Application

- [ ] Create `skills/application/TagSkillUseCase.java`.
  - Associates a skill with an application, ensuring idempotency.
- [ ] Create `skills/application/RemoveSkillTagUseCase.java`.
- [ ] Create `skills/application/ListApplicationSkillsUseCase.java`.

### Web / UI

- [ ] Add endpoints to a controller:
  - `POST /applications/{id}/skills` — to tag a skill.
  - `DELETE /applications/{appId}/skills/{skillId}` — to remove a tag.
  - `GET /applications/{id}/skills` — to list skills.
- [ ] Update application detail template:
  - Add a skill tagging section featuring a searchable skill dropdown, a chip display of tagged skills, and a remove button.
