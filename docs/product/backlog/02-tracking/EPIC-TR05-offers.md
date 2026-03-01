# EPIC-TR05: Offer Recording

**Bounded Context:** Tracking (Core)
**Priority:** P0

---

## Feature SPEC

### Problem

Users need a structured way to record formal job offers so they can evaluate, compare, and negotiate opportunities effectively. The `Offer` represents the successful culmination of the hiring pipeline and acts as a critical child entity of the `JobApplication` aggregate.

### Business Rules

- An `Offer` belongs to exactly one `JobApplication` (a child entity strictly governed by the aggregate root).
- **MVP Constraint:** There is a strictly 1-to-1 relationship between an application and an offer. Only one `Offer` can exist per application (enforced via a `UNIQUE` constraint on `job_application_id`).
- Required fields: offer date and status (defaults to `RECEIVED`).
- Optional fields: salary value, salary currency, contract type, and notes.
- `OfferStatus` has four valid states: `RECEIVED`, `ACCEPTED`, `DECLINED`, or `EXPIRED`.
- **Negotiation Flow:** Rather than creating a new offer entity for counter-offers, the user updates the existing offer. The `notes` field is used to capture the negotiation history and details.
- **Crucial Decoupling:** Recording or updating an offer does NOT automatically change the overall `JobApplication` status to `OFFER`. The user remains in full control of the pipeline state machine.
- **Aggregate Enforcement:** All write operations (create, update, change status) must go through the `JobApplication` aggregate root to ensure absolute data consistency.
- Key actions publish domain events (e.g., `OfferReceived`, `OfferUpdated`, `OfferStatusChanged`) to allow downstream contexts (like an Analytics or Compensation benchmarking module) to react asynchronously.

### Ubiquitous Language

| Term        | Definition                                                                                               |
| ----------- | -------------------------------------------------------------------------------------------------------- |
| Offer       | A formal job proposal containing compensation details, contract type, and its current acceptance status. |
| OfferStatus | The lifecycle state of the proposal: `RECEIVED`, `ACCEPTED`, `DECLINED`, or `EXPIRED`.                   |

---

## Vertical Slices

| ID      | Slice                                                        | Dependencies |
| ------- | ------------------------------------------------------------ | ------------ |
| TR05-S1 | [Create and Manage an Offer](TR05-S1-create-manage-offer.md) | TR01-S1      |
