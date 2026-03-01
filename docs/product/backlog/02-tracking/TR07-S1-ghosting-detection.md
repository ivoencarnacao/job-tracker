# Slice TR07-S1: Ghosting Policy Detection

**Epic:** [EPIC-TR07](EPIC-TR07-ghosting-policy.md)
**Priority:** P1
**Vertical Slice:** VS-08

---

## 🎯 Value Delivered

The system proactively identifies stale applications that are likely "ghosted" by employers. This enables the user to take proactive action (like following up or closing the application) without having to manually calculate how many days have passed since the last update.

## 🔗 Dependencies

- **TR01-S1** — Applications must exist to be evaluated.
- **TR02-S1** — The pipeline status state machine must be functional, as the detection relies on specific states (`APPLIED` and `UNDER_REVIEW`).
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce strict data isolation.

---

## 📖 User Story

As an **authenticated User**, I want the system to **automatically detect applications that have been inactive for too long**, so that **I can decide whether to follow up or mark them as ghosted without manual tracking**.

---

## ✅ Acceptance Criteria

**Scenario 1: Accurate Candidate Detection**

- The system evaluates applications to find those currently in the `APPLIED` or `UNDER_REVIEW` states.
- An application is only flagged as a candidate if its `updated_at` timestamp is older than the configured threshold (defaulting to 21 days).
- Applications in any other state (e.g., `HR_INTERVIEW`, `OFFER`, `REJECTED`) are strictly ignored, regardless of their age.

**Scenario 2: Strict Data Isolation**

- The detection query is strictly scoped to the authenticated user's `userId`.
- It is impossible for the system to evaluate or return ghosting candidates belonging to another user.

**Scenario 3: Non-Mutating Evaluation (Invariant)**

- The detection process is a purely read-only evaluation.
- The system must NEVER automatically mutate the `ApplicationStatus` to `GHOSTED` during this process. It merely identifies candidates for the user to review.

---

## 🛠️ Technical Tasks (Implementation)

### Domain

- [ ] Create `tracking/domain/GhostingPolicy.java` (Domain Service)
  - Implement `isGhostingCandidate(JobApplication application)`: evaluates the application's current status and `updatedAt` age against the rules.
  - Expose a configurable threshold (defaulting to 21 days).

### Infrastructure

- [ ] Update `tracking/infrastructure/JpaJobApplicationRepository.java`
  - Add a highly optimized read-only query to fetch candidates directly from the database (to avoid loading all applications into memory).
  - Example: `SELECT a FROM JpaJobApplicationEntity a WHERE a.userId = :userId AND a.status IN (:statuses) AND a.updatedAt < :thresholdDate`.

### Application

- [ ] Create `tracking/application/DetectGhostingCandidatesUseCase.java`
  - Extracts the `userId` from the `AuthenticatedUser`.
  - Calculates the `thresholdDate` (e.g., `now() - 21 days`).
  - Calls the repository query to fetch the stale applications.
  - Optionally passes the results through the `GhostingPolicy` domain service for final validation (if the logic is too complex for pure SQL) and returns a list of candidate DTOs.
