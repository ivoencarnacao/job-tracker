# Global User Journey

The end-to-end journey across all bounded contexts:

```
[IDENTITY]    1. Visitor registers and logs in
                  |
[TRACKING]    2. User creates first job application (title, company, status)
                  |
[TRACKING]    3. User updates pipeline status as hiring process evolves
                  |
[TRACKING]    4. User sets follow-up reminders on applications
                  |
[TRACKING]    5. User records interviews with dates, types, notes
                  |
[TRACKING]    6. User records offers with salary and status
                  |
[TRACKING]    7. User searches and filters applications
                  |
[DASHBOARD]   8. User views dashboard with metrics (total, by status, response rate)
                  |
[SKILLS]      9. User tags skills manually on applications
                  |
[SKILLS]     10. System extracts skills from job descriptions automatically
                  |
[DASHBOARD]  11. User views skill insights (top skills, category breakdown)
                  |
[TRACKING]   12. System suggests ghosting for stale applications (21 days)
```
