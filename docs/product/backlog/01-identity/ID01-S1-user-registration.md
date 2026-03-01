# Slice ID01-S1: User Registration

**Epic:** [EPIC-ID01](EPIC-ID01-user-registration.md)
**Priority:** P0
**Sprint:** 1

---

## 🎯 Value Delivered

A visitor can create an account and become an authenticated User. This is the first usable feature — all other slices depend on it.

## 🔗 Dependencies

- None (first slice in the system).

---

## 📖 User Story

As an **unauthenticated visitor**, I want to **create a new account using my basic details**, so that **I can become a registered user and access the platform**.

---

## ✅ Acceptance Criteria

**Scenario 1: Successful Registration**

- The visitor fills out the registration form with a valid email, password, and display name.
- The system creates the `User` aggregate with all required fields.
- The user is successfully persisted in the database.
- The UI redirects the user to the login page (or auto-logs them in) with a success message.

**Scenario 2: Email Uniqueness Invariant**

- The visitor attempts to register with an email that already exists in the system.
- The application/domain detects the duplication (`existsByEmail`) and prevents the registration, ensuring logical protection alongside the database `UNIQUE` constraint.
- The UI displays a friendly error message next to the email field.

**Scenario 3: Secure Password Hashing**

- When processing the registration, the system ensures the plain-text password never reaches the domain model.
- The password is hashed (via BCrypt) before the `User` aggregate is created and persisted.

**Scenario 4: Aggregate Completeness and Validation**

- The visitor submits the form with an invalid email format or a display name that exceeds the length limit.
- The `User` aggregate's factory method rejects the creation, guaranteeing the object never exists in an invalid state in memory.
- The UI displays the corresponding server-side validation errors via Thymeleaf.

**Scenario 5: Domain Event Publication**

- Upon successful persistence of the new user, the system publishes a `UserRegistered` domain event to notify other bounded contexts (as defined in the Epic rules).

---

## 🛠️ Technical Tasks (Implementation)

### Shared / Config

- [ ] Update `shared/config/SecurityConfig.java`
  - Permit `/register` endpoint
  - Configure BCrypt password encoder bean

### Database & Infra

- [ ] Create Flyway migration `V1__create_users_table.sql`
  - Schema `core`, table `users`
  - Columns: `id` (UUID PK), `email` (VARCHAR 255 UNIQUE), `password_hash` (VARCHAR 255), `display_name` (VARCHAR 100), `created_at` (TIMESTAMPTZ), `updated_at` (TIMESTAMPTZ)
- [ ] Create `identity/infrastructure/JpaUserEntity.java`
  - JPA entity mapped to `core.users`
- [ ] Create `identity/infrastructure/JpaUserRepository.java`
  - Spring Data JPA implementation of `UserRepository`
- [ ] Create `identity/infrastructure/UserMapper.java`
  - MapStruct mapper: domain `User` <-> JPA entity

### Domain

- [ ] Create `identity/domain/User.java`
  - Aggregate root with factory method
  - Email format validation, display name length validation
- [ ] Create `identity/domain/UserRepository.java`
  - Interface: `save(User)`, `findByEmail(String)`, `existsByEmail(String)`

### Application

- [ ] Create DTOs `identity/application/dto/RegisterUserInput.java` and `RegisterUserOutput.java`
- [ ] Create `identity/application/RegisterUserUseCase.java`
  - Validates email uniqueness via repository
  - Hashes password using injected encoder
  - Creates domain `User` aggregate and saves via repository
  - Publishes `UserRegistered` event

### Web / UI

- [ ] Create `identity/web/RegistrationController.java`
  - `GET /register` shows form
  - `POST /register` processes registration
- [ ] Create `templates/identity/register.html`
  - Registration form (email, password, display name)
  - Integration with Thymeleaf validation errors
