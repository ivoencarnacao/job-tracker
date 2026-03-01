# Slice SK02-S1: Skill Extraction Engine

**Epic:** [EPIC-SK02](EPIC-SK02-skill-extraction.md)
**Priority:** P1
**Vertical Slice:** VS-10

---

## 🎯 Value Delivered

Skills are automatically detected and extracted from pasted job descriptions, significantly reducing manual data entry work for the user. This background process is triggered via a domain event the moment a job description is saved, providing a seamless and intelligent user experience.

## 🔗 Dependencies

- **SK01-S1** — The skill catalog must exist to provide the keywords and aliases for extraction.
- **SK01-S2** — The `job_application_skills` table and associations must exist to store the extracted results.

---

## 📖 User Story

As an **authenticated User**, I want **the system to automatically read my job descriptions and extract the relevant skills**, so that **I don't have to spend time manually tagging every single technology mentioned**.

---

## ✅ Acceptance Criteria

**Scenario 1: Event-Driven Trigger and Idempotency**

- When a user creates or updates a job application with a description, the Tracking context publishes a `JobDescriptionUpdated` event.
- The Skills context listens to this event and asynchronously triggers the extraction process.
- The extraction process is strictly idempotent: it clears any previously auto-extracted skills for that application and re-extracts them based on the new text, ensuring the tags are always in sync with the latest description.

**Scenario 2: Accurate Extraction and Word Boundary Awareness**

- The `SkillExtractor` scans the text using case-insensitive matching.
- The extraction algorithm respects word boundaries (e.g., matching "C" only as a standalone word, not inside "React" or "Mac") to prevent false positives.

**Scenario 3: Normalization and Uniqueness (Invariant)**

- Raw text matches are passed to the `SkillNormalizer`, which resolves any matched aliases (e.g., "K8s") to their definitive canonical `Skill` entity (e.g., "Kubernetes").
- The system enforces that each canonical skill is associated with the application only once, regardless of how many times it was mentioned in the text.

---

## 🛠️ Technical Tasks (Implementation)

### Domain

- [ ] Create `skills/domain/SkillExtractor.java` (Domain Service)
  - Accepts the description text and the skill catalog.
  - Implements word boundary awareness and case-insensitive matching, returning a set of matched raw entities.
- [ ] Create `skills/domain/SkillNormalizer.java` (Domain Service)
  - Resolves the raw matches to canonical skills via aliases.
- [ ] Create `tracking/domain/event/JobDescriptionUpdated.java`
  - Plain Java record containing `(UUID applicationId, String description)`.

### Application

- [ ] Create `skills/application/ExtractSkillsUseCase.java`
  - Receives the application ID and description.
  - Executes the idempotent logic: clear existing auto-extracted tags -> run `SkillExtractor` -> run `SkillNormalizer` -> save new `JobSkill` associations.
- [ ] Create `tracking/application/DomainEventPublisher.java` (Interface)
  - Defines the contract `void publish(Object event)` for the Tracking context.
- [ ] Update `CreateJobApplicationUseCase` and `UpdateJobApplicationUseCase`
  - Inject the publisher and dispatch `JobDescriptionUpdated` when a description is provided or modified.

### Infrastructure

- [ ] Create `tracking/infrastructure/SpringDomainEventPublisher.java`
  - Implements the publisher interface using Spring's `ApplicationEventPublisher`.
- [ ] Create `skills/infrastructure/listener/SkillExtractionListener.java`
  - Spring component with an `@EventListener` (or `@Async @EventListener` / `@TransactionalEventListener` depending on the consistency needs) listening to `JobDescriptionUpdated`.
  - Delegates the payload to `ExtractSkillsUseCase`.
