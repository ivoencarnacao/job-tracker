# EPIC-ID01: User Registration & Authentication

**Bounded Context:** Identity (Supporting)
**Priority:** P0

---

## Feature SPEC

### Problem

The system is multi-user with strict data isolation. Without authentication, there is no way to scope data per user. Every other bounded context depends on knowing who the current User is.

### Business Rules

- A Visitor registers with email, password, and display name.
- Email must be unique across all Users.
- Password is stored as a BCrypt hash, never in plain text.
- After registration, the User is automatically logged in and redirected to the Dashboard.
- A logged-in User can only see and modify their own data (strict data isolation).
- Duplicate email registration shows an error and suggests login.
- *(P1)* Upon successful registration, the system publishes a `UserRegistered` domain event to notify other bounded contexts.

### Ubiquitous Language

| Term    | Definition                                                          |
| ------- | ------------------------------------------------------------------- |
| Visitor | An unauthenticated person browsing the system.                      |
| User    | Registered person with private, isolated data.                      |
| Session | The temporary state of an authenticated User with validated access. |

---

## Vertical Slices

| ID      | Slice                                                    | Dependencies |
| ------- | -------------------------------------------------------- | ------------ |
| ID01-S1 | [User Registration](ID01-S1-user-registration.md)        | None         |
| ID01-S2 | [Login and Session Management](ID01-S2-login-session.md) | ID01-S1      |
