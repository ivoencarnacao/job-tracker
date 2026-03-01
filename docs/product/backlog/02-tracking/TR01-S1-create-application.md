# Slice TR01-S1: Create a Job Application

**Epic:** [EPIC-TR01](EPIC-TR01-application-crud.md)
**Priority:** P0
**Sprint:** 1

---

## 🎯 Value Delivered

An authenticated User can register a new job application and view their list of saved applications. This is the foundation for all Tracking functionality and the first real business feature after authentication.

## 🔗 Dependencies

- **ID01-S1 & ID01-S2** — Requires an authenticated user and the `AuthenticatedUser` component to extract the `userId`.

---

## 📖 User Story

As an **authenticated User**, I want to **register a new job application and view my application list**, so that **I can start tracking my job hunt effectively**.

---

## ✅ Acceptance Criteria

**Scenario 1: Successful Application Creation**

- The user navigates to the new application form and fills in, at minimum, the required fields (Title and Company).
- If the user does not select a status, the system defaults the status to `SAVED`.
- The system creates the `JobApplication` aggregate and saves it to the database, mapping all enum value objects correctly.
- The UI redirects the user to the application list with a success message.

**Scenario 2: Strict Data Isolation**

- The created `JobApplication` is intrinsically bound to the logged-in user's `userId` (extracted securely via the `AuthenticatedUser` component).
- When viewing the application list, the user only sees their own applications.

**Scenario 3: Domain Invariants and Validation**

- If the user provides a minimum and maximum salary, the system enforces that `salaryMin <= salaryMax` before persistence.
- If provided, URLs (e.g., job link) and emails (e.g., recruiter contact) are validated against correct format rules.
- If any validation fails (e.g., missing title or invalid salary range), the UI rejects the submission and displays specific validation errors next to the affected fields.

**Scenario 4: Domain Event Publication**

- Upon successful creation of the application, if the user provided text in the job description field, the system publishes the `JobDescriptionUpdated` domain event.

---

## 🛠️ Technical Tasks (Implementation)

### Database & Infra

- [ ] Create Flyway migration `V2__create_job_applications_table.sql`
  - All columns per `DATA-SCHEMA-DETAIL.md`
  - PostgreSQL enum types: `application_status`, `work_mode`, `contract_type`
  - FK to `core.users`, check constraint on salary, indexes
- [ ] Create `tracking/infrastructure/JpaJobApplicationEntity.java`
  - JPA entity mapped to `core.job_applications`
  - PostgreSQL enum mapping with `@Enumerated(EnumType.STRING)` + `PostgreSQLEnumJdbcType`
- [ ] Create `tracking/infrastructure/JpaJobApplicationRepository.java`
  - Spring Data JPA implementation
- [ ] Create `tracking/infrastructure/JobApplicationMapper.java`
  - MapStruct mapper: domain <-> JPA entity

### Domain

- [ ] Create `tracking/domain/ApplicationStatus.java` (Enum with all 11 states)
- [ ] Create `tracking/domain/WorkMode.java` and `tracking/domain/ContractType.java` (Value object enums)
- [ ] Create `tracking/domain/JobApplication.java`
  - Aggregate root with factory method `create(userId, title, company, status)`
  - Validation in constructor (title/company required, salary range check, URL/email format)
- [ ] Create `tracking/domain/JobApplicationRepository.java`
  - Interface: `save`, `findById`, `findAllByUserId`

### Application

- [ ] Create DTOs `CreateJobApplicationInput.java` and `JobApplicationOutput.java`
- [ ] Create `tracking/application/CreateJobApplicationUseCase.java`
  - Receives input DTO, creates domain aggregate using factory method, saves
  - Publishes `JobDescriptionUpdated` event if description is provided
- [ ] Create `tracking/application/ListJobApplicationsUseCase.java`
  - Fetches applications via `findAllByUserId` and returns output DTOs

### Web / UI

- [ ] Create `tracking/web/JobApplicationController.java`
  - `GET /applications/new` shows creation form
  - `POST /applications` processes form submission
  - `GET /applications` shows the user's list of applications
- [ ] Create `templates/tracking/application-form.html`
  - Form with all fields, status dropdown, Thymeleaf validation errors
- [ ] Create `templates/tracking/application-list.html`
  - Table/card view listing the user's applications
