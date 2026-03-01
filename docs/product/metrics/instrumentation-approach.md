# Instrumentation Approach

## Server-Side (Domain Events)

Most events align naturally with existing domain events in the backlog:

| Application Event | Domain Event Source |
|-------------------|-------------------|
| `user.registered` | `UserRegistered` (ID01-S1) |
| `application.created` | Use case completion in `CreateJobApplicationUseCase` |
| `application.status_updated` | `ApplicationStatusChanged` (TR02-S1) |
| `follow_up.created` | `FollowUpScheduled` (TR03-S1) |
| `follow_up.completed` | `FollowUpCompleted` (TR03-S2) |
| `interview.created` | `InterviewScheduled` (TR04-S1) |
| `offer.created` | `OfferReceived` (TR05-S1) |
| `skill.extracted` | `ExtractSkillsUseCase` completion (SK02-S1) |

## Client-Side (Plausible/Umami)

Page view tracking handled by analytics script on every page:

| Page | What It Tells Us |
|------|-----------------|
| `/` (landing) | Top of funnel — visitor volume |
| `/register` | Intent to sign up |
| `/dashboard` | Active usage, session tracking |
| `/applications/new` | Intent to create (H1 start timestamp) |
| `/insights` | Feature adoption (H3 segmentation) |

## Storage

MVP approach: analytics events stored as rows in a simple `analytics_events` table or derived from existing domain tables via SQL queries during analysis. No separate analytics infrastructure needed for Private Beta (10-20 users).
