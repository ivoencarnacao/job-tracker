# EPIC-SK02: Automatic Skill Extraction

**Bounded Context:** Skills (Core)
**Priority:** P1

---

## Feature SPEC

### Problem

Manually tagging skills is a tedious process for the user. The system should automatically extract relevant skills directly from pasted job descriptions using intelligent keyword matching against the established skill catalog.

### Business Rules

- **Extraction Engine:** A `SkillExtractor` domain service is responsible for parsing the job description text and finding skill mentions using keyword matching (covering both canonical names and aliases).
- **Normalization:** A `SkillNormalizer` domain service maps these raw text matches back to their exact canonical `Skill` entities.
- **Event-Driven Trigger:** The extraction process is asynchronous and triggered automatically by the `JobDescriptionUpdated` domain event originating from the Tracking context.
- **Idempotency/Deduplication:** Each extracted skill is counted exactly once per application, strictly preventing duplicate entries.
- **Matching Constraints:** The matching algorithm must be case-insensitive and feature word boundary awareness to prevent false positives (e.g., avoiding matching the letter "C" inside the word "React").
- **Capacity Limits:** The extraction engine must robustly support parsing job descriptions up to 50,000 characters in length.
- **Fallback State:** If no skills are detected by the engine, the system must show a clear feedback message to the user and seamlessly offer the manual tagging alternative.

### Ubiquitous Language

| Term            | Definition                                                                                                |
| --------------- | --------------------------------------------------------------------------------------------------------- |
| SkillExtractor  | A domain service that parses job description text to accurately find skill mentions based on the catalog. |
| SkillNormalizer | A domain service that maps raw text matches back to their standardized canonical `Skill` entities.        |

---

## Vertical Slices

| ID      | Slice                                                               | Dependencies     |
| ------- | ------------------------------------------------------------------- | ---------------- |
| SK02-S1 | [Skill Extraction Engine](SK02-S1-skill-extraction-engine.md)       | SK01-S1, SK01-S2 |
| SK02-S2 | [Extraction Results Display](SK02-S2-extraction-results-display.md) | SK02-S1          |
