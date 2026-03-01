# Sitemap & Navigation

## Public Pages (Unauthenticated)

| Route       | Page              | Purpose                        |
| ----------- | ----------------- | ------------------------------ |
| `/`         | Landing page      | Value proposition, sign-up CTA |
| `/register` | Registration form | Email, password, display name  |
| `/login`    | Login form        | Email, password                |

## Authenticated Pages

| Route                       | Page                | Vertical Slice | Purpose                                 |
| --------------------------- | ------------------- | -------------- | --------------------------------------- |
| `/dashboard`                | Dashboard           | VS-07          | Core metrics, alerts, upcoming activity |
| `/applications`             | Application list    | VS-02          | Browse, search, filter applications     |
| `/applications/new`         | Create application  | VS-02          | New application form                    |
| `/applications/{id}`        | Application detail  | VS-02          | Full view with child entities           |
| `/applications/{id}/edit`   | Edit application    | VS-02          | Pre-populated edit form                 |
| `/applications/{id}/delete` | Delete confirmation | VS-03          | Cascade impact warning                  |
| `/insights`                 | Skill insights      | VS-11 (P1)     | Top skills, category breakdown          |

## Primary Navigation

```
[Logo/Home] — [Dashboard] — [Applications] — [Insights*] — [Logout]
```

> \*Insights appears only after Skills context is implemented (VS-09+).

## Navigation Flow

```
Landing (/) → Register → Login → Dashboard
                                     |
                                     +→ Applications List
                                     |      |
                                     |      +→ New Application
                                     |      +→ Application Detail
                                     |             |
                                     |             +→ Edit
                                     |             +→ Delete Confirm
                                     |             +→ Follow-ups (inline)
                                     |             +→ Interviews (inline)
                                     |             +→ Offer (inline)
                                     |             +→ Skills (inline, P1)
                                     |
                                     +→ Insights (P1)
```

## Application Detail — Inline Sections

These are not separate pages — they are sections and actions within the application detail page.

| Section    | Actions                                                           | Vertical Slice |
| ---------- | ----------------------------------------------------------------- | -------------- |
| Status     | Dropdown with valid transitions → PATCH /applications/{id}/status | VS-03          |
| Follow-ups | Add form + list (ordered by due date, overdue highlighted)        | VS-04          |
| Interviews | Add form + list (type, date, outcome)                             | VS-05          |
| Offer      | Create or edit form (1-to-1, adapts based on existence)           | VS-05          |
| Skills     | Tag dropdown + chips with remove buttons                          | VS-09 (P1)     |

## API Routes (Non-Page)

Actions that don't render pages — they process form submissions and redirect.

| Method | Route                                            | Slice   | Purpose                  |
| ------ | ------------------------------------------------ | ------- | ------------------------ |
| POST   | `/register`                                      | ID01-S1 | Process registration     |
| POST   | `/applications`                                  | TR01-S1 | Create application       |
| PUT    | `/applications/{id}`                             | TR01-S2 | Update application       |
| DELETE | `/applications/{id}`                             | TR01-S3 | Delete application       |
| PATCH  | `/applications/{id}/status`                      | TR02-S1 | Update pipeline status   |
| POST   | `/applications/{id}/follow-ups`                  | TR03-S1 | Add follow-up            |
| PATCH  | `/applications/{appId}/follow-ups/{id}/complete` | TR03-S2 | Complete follow-up       |
| POST   | `/applications/{id}/interviews`                  | TR04-S1 | Add interview            |
| PUT    | `/applications/{appId}/interviews/{id}`          | TR04-S1 | Update interview outcome |
| POST   | `/applications/{id}/offer`                       | TR05-S1 | Create offer             |
| PUT    | `/applications/{id}/offer`                       | TR05-S1 | Update offer             |
| POST   | `/applications/{id}/skills`                      | SK01-S2 | Tag skill                |
| DELETE | `/applications/{appId}/skills/{skillId}`         | SK01-S2 | Remove skill tag         |
