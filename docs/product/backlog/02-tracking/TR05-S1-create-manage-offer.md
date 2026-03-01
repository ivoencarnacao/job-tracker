# Slice TR05-S1: Create and Manage an Offer

**Epic:** [EPIC-TR05](EPIC-TR05-offers.md)
**Priority:** P0
**Vertical Slice:** VS-05

---

## 🎯 Value Delivered

An authenticated User can record a formal job offer with salary and status details, and update it as negotiations evolve. This provides a clear, central record of compensation to help the user evaluate and compare opportunities effectively.

## 🔗 Dependencies

- **TR01-S1 & TR01-S2** — A `JobApplication` must exist to attach an offer, and the detail view is needed to manage it.
- **ID01-S2** — Requires the `AuthenticatedUser` component to enforce ownership.

---

## 📖 User Story

As an **authenticated User**, I want to **record and manage a job offer for my application**, so that **I can evaluate the compensation, track negotiation details, and monitor its acceptance status**.

---

## ✅ Acceptance Criteria

**Scenario 1: Successful Offer Registration**

- The user navigates to an application without an existing offer and submits the offer form.
- The system validates that the offer date and status are provided (status defaults to `RECEIVED` if omitted).
- The new `Offer` is attached to the `JobApplication` aggregate.
- The system persists the aggregate, saving the child entity correctly via cascade.

**Scenario 2: One-to-One Invariant Enforcement**

- The `JobApplication` aggregate strictly enforces that only one `Offer` can exist at a time.
- If an offer already exists, the `addOffer` method throws a domain exception.
- The database schema reinforces this rule with a `UNIQUE` constraint on `job_application_id`.
- The UI dynamically adapts: if an offer exists, it displays the "Edit/Update" form rather than the "Create" form.

**Scenario 3: Offer Update and Negotiation**

- The user updates the details of an existing offer (e.g., changing the salary after negotiation or updating the status to `ACCEPTED`).
- The user can use the `notes` field to log the negotiation history.
- The system updates the existing `Offer` entity through the `JobApplication.updateOffer(...)` method and saves the aggregate.

**Scenario 4: Strict Pipeline Decoupling**

- Creating or updating an offer does NOT automatically change the `JobApplication`'s overall `ApplicationStatus` (e.g., to `OFFER`).
- The user retains full manual control over the pipeline state machine.

**Scenario 5: Domain Event Publication**

- Upon successfully registering a new offer, the system publishes an `OfferReceived` domain event.
- Upon updating an existing offer (e.g., changing its status or salary), the system publishes an `OfferUpdated` domain event.

---

## 🛠️ Technical Tasks (Implementation)

### Database & Infra

- [ ] Create Flyway migration `V5__create_offers_table.sql`
  - Table `core.offers`.
  - PostgreSQL enum type: `offer_status` (values: `RECEIVED`, `ACCEPTED`, `DECLINED`, `EXPIRED`).
  - Columns per `DATA-SCHEMA-DETAIL.md`, FK to `job_applications`, and a `UNIQUE` constraint on `job_application_id`.
- [ ] Update `tracking/infrastructure/JpaJobApplicationEntity.java`
  - Add `@OneToOne` mapping for `JpaOfferEntity` with `CascadeType.ALL` and `orphanRemoval = true`.
- [ ] Create `tracking/infrastructure/JpaOfferEntity.java`
  - Map properties and handle the `OfferStatus` enum mapping.

### Domain

- [ ] Create Enum: `tracking/domain/OfferStatus.java`.
- [ ] Create `tracking/domain/Offer.java` (Child Entity)
  - Factory method with validation (requires date and status).
- [ ] Update `tracking/domain/JobApplication.java` (Aggregate Root)
  - Add `addOffer(Offer offer)` method (must verify that the internal offer reference is currently null).
  - Add `updateOffer(...)` method to apply changes to the existing offer.
- [ ] Create Domain Events: `tracking/domain/event/OfferReceived.java` and `OfferUpdated.java`.

### Application

- [ ] Create DTOs `AddOfferInput.java`, `UpdateOfferInput.java`, and `OfferOutput.java`.
- [ ] Create `tracking/application/AddOfferUseCase.java`
  - Loads `JobApplication`, verifies ownership, calls `addOffer()`, saves aggregate, and publishes `OfferReceived`.
- [ ] Create `tracking/application/UpdateOfferUseCase.java`
  - Loads `JobApplication`, verifies ownership, calls `updateOffer()`, saves aggregate, and publishes `OfferUpdated`.
- [ ] Create `tracking/application/GetOfferUseCase.java`
  - Loads the `JobApplication` by ID and returns its parsed offer as a DTO (if it exists).

### Web / UI

- [ ] Update `tracking/web/JobApplicationController.java` (or create an `OfferController`)
  - `POST /applications/{id}/offer` — handles creation.
  - `PUT /applications/{id}/offer` (or `PATCH`) — handles updates.
  - `GET /applications/{id}/offer` — retrieves the offer to populate the UI.
- [ ] Update `templates/tracking/application-detail.html`
  - Add the Offer section.
  - Implement view logic: show the creation form if no offer exists, or display the current offer details with an "Edit" toggle if it does.
