# Slice SK01-S1: Skill Catalog Seed Data

**Epic:** [EPIC-SK01](EPIC-SK01-skill-catalog.md)
**Priority:** P1
**Vertical Slice:** VS-09

---

## 🎯 Value Delivered

The system is initialized with a comprehensive, standardized dictionary of professional skills. This establishes a robust foundation for all subsequent Skills context functionality, enabling accurate manual tagging and future automated extraction without data fragmentation.

## 🔗 Dependencies

- **ID01-S1** — The base database schema and authentication structures must exist.

---

## 📖 User Story

As an **authenticated User**, I want **the system to provide a standardized catalog of professional skills**, so that **I can accurately tag my applications and analyze my technical profile against market demands**.

---

## ✅ Acceptance Criteria

**Scenario 1: Canonical Name and Category Enforcement**

- The system requires every `Skill` to have a strictly unique canonical name.
- Every skill must be assigned to exactly one valid `SkillCategory` (e.g., `LANGUAGE`, `FRAMEWORK`).

**Scenario 2: Alias Resolution and Uniqueness**

- A skill can have multiple aliases (e.g., "Postgres", "psql" for "PostgreSQL").
- The system enforces alias uniqueness globally: an alias maps to exactly one canonical skill, preventing collision or ambiguity during search and extraction.

**Scenario 3: Seed Data Availability**

- Upon database initialization, the system automatically seeds approximately 100 common technical skills across all 10 categories.
- This seed data is immediately available for search and tagging operations.

---

## 🛠️ Technical Tasks (Implementation)

### Database & Infra

- [ ] Create Flyway migration `V6__create_skills_tables.sql`
  - PostgreSQL enum type: `skill_category`.
  - Table `core.skills` (id, name, category, created_at, updated_at).
  - Table `core.skill_aliases` (id, skill_id, alias, created_at).
  - Apply unique constraints on the `name` column and the `alias` column.
- [ ] Create Flyway migration `V7__seed_skill_catalog.sql`
  - Insert the initial ~100 skills (e.g., Java, Spring Boot, PostgreSQL, Docker) and their common aliases.
- [ ] Create `skills/infrastructure/JpaSkillEntity.java` and `JpaSkillAliasEntity.java`
  - Map properties and associations.
- [ ] Create `skills/infrastructure/JpaSkillRepository.java` and `SkillMapper.java`

### Domain

- [ ] Create `skills/domain/SkillCategory.java` (Enum with the 10 defined categories).
- [ ] Create `skills/domain/SkillAlias.java` (Value Object).
- [ ] Create `skills/domain/Skill.java` (Aggregate Root)
  - Includes canonical name, category, and a collection of `SkillAlias` objects.
- [ ] Create `skills/domain/SkillRepository.java`
  - Interface methods: `findByName`, `findByAlias`, `findAll`, `findByCategory`.

### Application

- [ ] Create DTOs for skill queries.
- [ ] Create `skills/application/SearchSkillsUseCase.java` (or `ListSkillsUseCase`)
  - Implements the logic to query the catalog by name, alias, or category, returning results for auto-completion or listing.

### Web / UI

- [ ] Create `skills/web/SkillController.java`
  - Expose query endpoints (e.g., `GET /skills` or `GET /skills/search`) to serve the catalog to the frontend for dropdowns or tag inputs.
