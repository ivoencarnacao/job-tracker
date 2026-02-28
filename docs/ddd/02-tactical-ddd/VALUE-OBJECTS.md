# Value Objects / Enums

All enums stored as PostgreSQL native enum types, mapped in JPA with `@Enumerated(EnumType.STRING)` + Hibernate 6 `PostgreSQLEnumJdbcType`.

## Tracking Context

| Type | Values |
|------|--------|
| ApplicationStatus | SAVED, APPLIED, UNDER_REVIEW, HR_INTERVIEW, TECHNICAL_INTERVIEW, CHALLENGE, FINAL_INTERVIEW, OFFER, REJECTED, GHOSTED, WITHDRAWN |
| WorkMode | REMOTE, HYBRID, ON_SITE |
| ContractType | FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP |
| InterviewType | HR, TECHNICAL, FINAL |
| InterviewFormat | ONLINE, ON_SITE |
| InterviewOutcome | PENDING, PASSED, FAILED |
| FollowUpOutcome | RESPONDED, NO_RESPONSE |
| OfferStatus | RECEIVED, ACCEPTED, DECLINED, EXPIRED |

## Skills Context

| Type | Values |
|------|--------|
| SkillCategory | LANGUAGE, FRAMEWORK, DATABASE, CLOUD, DEVOPS, TESTING, ARCHITECTURE, TOOL, METHODOLOGY, SOFT_SKILL |
