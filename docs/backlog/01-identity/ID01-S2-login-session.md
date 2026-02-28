# Slice ID01-S2: Login and Session Management

**Epic:** [EPIC-ID01](EPIC-ID01-user-registration.md)
**Priority:** P0
**Sprint:** 1

---

## 🎯 Value Delivered

A registered User can log in and maintain an authenticated session. This enables all authenticated features across the system and guarantees strict data isolation.

## 🔗 Dependencies

- **ID01-S1** — User must exist to log in.

---

## 📖 User Story

As a **registered User**, I want to **log in with my credentials**, so that **I can securely access my private dashboard and manage my job applications**.

---

## ✅ Acceptance Criteria

**Scenario 1: Successful Login**

- The user navigates to the login page and enters a valid email and matching password.
- The system authenticates the user and establishes a secure session.
- The user is automatically redirected to the default success URL (`/dashboard`).

**Scenario 2: Invalid Credentials Rejection**

- The user attempts to log in with an unregistered email or an incorrect password.
- The system denies access and does not expose which part of the credential pair was wrong (to prevent enumeration attacks).
- The UI redirects the user back to the login page displaying a generic "Invalid email or password" error message.

**Scenario 3: Secure Session Lifecycle and Logout**

- An authenticated user can access protected routes across the system.
- When the user triggers the logout action, the session is completely invalidated.
- After logout, the user is redirected to the `/login` page and can no longer access protected routes.

**Scenario 4: Cross-Context Identity Resolution**

- When an authenticated user performs an action in any bounded context, the system provides a reliable, lightweight mechanism (`AuthenticatedUser`) to extract the current `userId`.
- This ensures other contexts can scope their queries and commands strictly to the logged-in user without coupling to Spring Security internals.

---

## 🛠️ Technical Tasks (Implementation)

### Shared / Config

- [ ] Update `shared/config/SecurityConfig.java`
  - Enable form login with custom `/login` page
  - Set default success URL to `/dashboard`
  - Configure logout support with redirect to `/login`
  - Ensure protected routes require authentication

### Infrastructure

- [ ] Create `identity/infrastructure/JpaUserDetailsService.java`
  - Implement Spring Security `UserDetailsService` interface
  - Load User domain entity by email using `UserRepository`
  - Map domain User to Spring Security `UserDetails` object

### Application

- [ ] Create `identity/application/AuthenticatedUser.java`
  - Lightweight helper/facade to safely extract `userId` from `SecurityContext`
  - Acts as the standard identity provider for all other bounded contexts

### Web / UI

- [ ] Create `identity/web/LoginController.java`
  - `GET /login` endpoint to render the login form
- [ ] Create `templates/identity/login.html`
  - Login form (email, password inputs)
  - Integration with Thymeleaf to display Spring Security error messages
