# Domain Services

## Tracking Context

### PipelineService

- Responsibility: Enforces valid status transitions
- Methods:
  - `validateTransition(currentStatus, newStatus)` — throws if invalid
  - `getValidTransitions(currentStatus)` — returns allowed target states
- Rules:
  - REJECTED and WITHDRAWN are terminal (no transitions out)
  - GHOSTED is reversible (can transition to any active state)
  - OFFER can transition to REJECTED or WITHDRAWN
  - All other non-terminal states can transition to any non-terminal state

### GhostingPolicy

- Responsibility: Identifies applications likely ghosted
- Methods:
  - `isGhostingCandidate(application)` — checks status and updatedAt age
- Rules:
  - Only APPLIED and UNDER_REVIEW qualify
  - Threshold: 21 days without status change
  - Suggestion only — never automatic

## Skills Context

### SkillExtractor

- Responsibility: Parses job description text to find skill mentions
- Input: Description text + skill catalog (names + aliases)
- Output: Set of matched Skill entities
- Rules: Case-insensitive, word boundary awareness, descriptions up to 50,000 chars

### SkillNormalizer

- Responsibility: Maps raw text matches to canonical Skill entities
- Input: Raw text matches
- Output: Canonical Skill references (via alias resolution)

### TrendAnalyzer

- Responsibility: Computes frequency, top-N, and category groupings
- Input: Raw skill-application association data
- Output: Ranked skill list, category breakdown, frequency percentages
