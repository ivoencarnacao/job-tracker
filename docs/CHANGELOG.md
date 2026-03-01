# Changelog

All notable changes to project documentation are documented in this file.

---

## 2026-03-01 — Combined product and implementation roadmap

Added `docs/product/ROADMAP.md` — a single document that synthesizes MVP-SCOPE, GTM-STRATEGY, EPIC-MAP, JOURNEY, and METRICS-PLAN into a unified view.

- Sprint plan (Sprints 1-6) with slice assignments, Flyway versions, and deliverables per sprint
- Dependency map showing blocking relationships between all 23 slices
- Launch phases (Private Beta → Open Beta → Public Launch) with entry gates and success criteria
- Release criteria checklist (functional, technical, operational)
- Metrics by phase (product metrics, acquisition metrics, hypothesis validation schedule)
- Future backlog (P2 features)

---

## 2026-03-01 — Cross-document consistency fixes

Reviewed product docs, strategic DDD, and tactical DDD for consistency. Fixed 7 inconsistencies (5 major, 2 moderate).

### Major fixes

1. **Domain Events gap** (`02-tactical-ddd/DOMAIN-EVENTS.md`)
   - Added 6 missing events: `UserRegistered`, `JobApplicationCreated`, `FollowUpScheduled`, `FollowUpCompleted`, `InterviewScheduled`, `OfferReceived`
   - Previously only `JobDescriptionUpdated` and `ApplicationStatusChanged` were defined; product docs (`metrics/instrumentation-approach.md`) expected 8
   - Changed `ApplicationStatusChanged` subscriber from "Dashboard (future)" to "Dashboard (metrics)"

2. **SkillCategory missing SOFT_SKILL** (`01-strategic-ddd/subdomains.md`)
   - Added "Soft Skill" to the skill category taxonomy (was listing 9 categories; product docs and tactical DDD both list 10)

3. **JobSkill write path undefined** (`02-tactical-ddd/AGGREGATES.md`, `02-tactical-ddd/CROSS-CONTEXT.md`)
   - Added `JobSkill` as a cross-context association entity in `AGGREGATES.md` under Skills context, with fields, ownership, creation triggers, and repository
   - Added two rows to `CROSS-CONTEXT.md` Communication Map: `JobApplicationCreated` event (Tracking → Skills) and JobSkill write ownership (Skills → Tracking table)

4. **Tracking→Skills relationship type** (`01-strategic-ddd/context-map.md`)
   - Changed from "Conformist" to "Customer-Supplier" — Skills does not conform to Tracking's model; it receives primitive data via domain events and processes it with its own logic
   - Updated diagram label (CF → CS), legend, section title, and description

5. **OFFER listed as terminal state** (`01-strategic-ddd/ubiquitous-language.md`)
   - Corrected Domain Rule #4: OFFER is not terminal — it can transition to Rejected or Withdrawn (aligned with PipelineService in tactical DDD and RF1 in product docs)

### Moderate fixes

6. **ApplicationStatusChanged subscriber** (`02-tactical-ddd/DOMAIN-EVENTS.md`)
   - Addressed as part of fix #1 — changed "(future)" to "(metrics)"

7. **Recruiter modeling decision** (`02-tactical-ddd/AGGREGATES.md`)
   - Added design note explaining that recruiter data is stored as flat fields (recruiterName, recruiterCompany, recruiterEmail) rather than a separate value object
