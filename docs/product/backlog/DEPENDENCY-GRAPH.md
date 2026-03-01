# Dependency Graph

Slice-level dependencies. Arrows indicate "must complete before".

```
ID01-S1 (Registration)
  |
  +-- ID01-S2 (Login)
  |
  +-- TR01-S1 (Create Application) ----------- SK01-S1 (Skill Catalog)
        |                                        |
        +-- TR01-S2 (View/Edit)                  +-- SK01-S2 (Manual Tagging)
        |                                        |       |
        +-- TR01-S3 (Delete)                     |       +-- SK02-S1 (Extraction Engine)
        |                                        |       |       |
        +-- TR02-S1 (Pipeline)                   |       |       +-- SK02-S2 (Extraction UI)
        |       |                                |       |
        +-- TR03-S1 (Create FollowUp)            |       +-- SK03-S1 (Frequency/Top Skills)
        |       |                                |               |
        |       +-- TR03-S2 (Complete)           |               +-- DA02-S1 (Insights Page)
        |                                        |
        +-- TR04-S1 (Interviews)                 |
        |                                        |
        +-- TR05-S1 (Offers)                     |
        |                                        |
        +-- TR06-S1 (Search)                     |
        |       |                                |
        |       +-- TR06-S2 (Status Filter)      |
        |               |                        |
        |               +-- TR06-S3 (Filters)    |
        |                                        |
        +-- DA01-S1 (Count Metrics)              |
                |                                |
                +-- DA01-S2 (Response Rate)      |
                |     [needs TR03-S1, TR04-S1]   |
                |                                |
                +-- DA01-S3 (No-Response Alerts) |
                |                                |
                +-- TR07-S1 (Ghosting Detection) |
                        |  [needs TR02-S1]       |
                        |                        |
                        +-- TR07-S2 (Ghosting UI)|
```

## Key Dependencies

| Slice | Depends On | Reason |
|-------|-----------|--------|
| ID01-S2 | ID01-S1 | User must exist to log in |
| TR01-S1 | ID01-S1 | Needs authenticated userId |
| TR02-S1 | TR01-S1 | JobApplication must exist for status change |
| TR03-S1 | TR01-S1 | FollowUp belongs to a JobApplication |
| TR04-S1 | TR01-S1 | Interview belongs to a JobApplication |
| TR05-S1 | TR01-S1 | Offer belongs to a JobApplication |
| DA01-S2 | TR03-S1, TR04-S1 | Needs follow-ups and interviews data |
| SK01-S2 | SK01-S1, TR01-S1 | Needs skill catalog and applications |
| SK02-S1 | SK01-S1, SK01-S2 | Needs catalog + JobSkill table |
| SK03-S1 | SK01-S2 | Needs skill-application associations |
| DA02-S1 | SK03-S1 | Needs skill computation logic |
| TR07-S1 | TR02-S1 | Needs pipeline status transitions |
