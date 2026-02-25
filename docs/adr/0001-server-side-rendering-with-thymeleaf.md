# ADR-0001: Server-Side Rendering with Thymeleaf

## Status

Accepted

## Context

The MVP needs a UI layer. The main options are:

1. **Single-Page Application (SPA)** with a JavaScript framework (React, Vue, Angular) consuming a REST/JSON API.
2. **Server-Side Rendering (SSR)** with a template engine integrated into Spring Boot.

The team is a single developer with strong Java/Spring experience. The MVP prioritizes shipping speed, simplicity, and maintainability over rich client-side interactivity.

## Decision

Use **Thymeleaf** with the Layout Dialect for server-side HTML rendering, served directly from Spring MVC controllers.

## Consequences

### Positive

- **Single codebase and deployment** — no separate frontend build pipeline or deployment artifact.
- **No API layer needed for MVP** — controllers return rendered HTML, eliminating the need to design, version, and document a REST API.
- **Natural Spring Security integration** — form login, CSRF tokens, and session management work out of the box with server-rendered forms.
- **Faster initial page loads** — HTML is fully rendered on the server; no JavaScript bundle to download and parse.
- **Simpler mental model** — request/response cycle without client-side state management (Redux, Zustand, etc.).
- **Strong Spring ecosystem support** — Thymeleaf is a first-class citizen in Spring Boot with hot reload, test utilities, and extensive documentation.

### Negative

- **Less interactive UI** — page transitions require full reloads unless enhanced with HTMX or Turbo (not in MVP scope).
- **Harder to add rich interactivity later** — if the product evolves to need complex client-side interactions, migrating to an SPA would be significant work.
- **Template logic can become messy** — complex conditional rendering in Thymeleaf is less ergonomic than JSX/Vue templates.

### Mitigation

- HTMX or similar can be added incrementally for specific interactive elements without rewriting the entire frontend.
- The clean separation between `web/` controllers and `application/` use cases means an API layer can be added alongside Thymeleaf if needed.
