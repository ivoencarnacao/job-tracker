# Future Backlog Items

Items beyond MVP scope, to consider in future iterations.

## Event Infrastructure Evolution

- **Async domain events:** Add `@Async` + `@EnableAsync` for non-blocking event processing
- **Post-commit events:** Use `@TransactionalEventListener(phase = AFTER_COMMIT)` to ensure events fire only after successful transaction commit

## P2 Features

- **Temporal skill trends:** Frequency changes over 30/60 days (time-series analysis)
- **Recruiter contact management:** Store and manage recruiter details per application
- **Salary comparison:** Side-by-side offer comparison view
- **CSV export:** Export application data for external analysis
- **Additional filters:** Source, work mode filters on application list

## Technical Improvements

- **Optimistic locking:** Add version column for concurrent edit detection (multiple tabs scenario)
- **Account deletion:** Full cascade delete of all user data with password confirmation
- **Bulk operations:** Multi-select and bulk status change / delete
- **Full-text search:** PostgreSQL `tsvector`/`tsquery` for richer search capabilities
