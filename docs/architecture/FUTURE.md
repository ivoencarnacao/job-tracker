# Future Technical Improvements

Technical improvements beyond MVP scope, to consider in future iterations.

> Product features (skill trends, recruiter management, CSV export) are tracked in [product/backlog/FUTURE.md](../product/backlog/FUTURE.md).

## Event Infrastructure Evolution

- **Async domain events:** Add `@Async` + `@EnableAsync` for non-blocking event processing
- **Post-commit events:** Use `@TransactionalEventListener(phase = AFTER_COMMIT)` to ensure events fire only after successful transaction commit

## Infrastructure

- **Optimistic locking:** Add version column for concurrent edit detection (multiple tabs scenario)
- **Full-text search:** PostgreSQL `tsvector`/`tsquery` for richer search capabilities
