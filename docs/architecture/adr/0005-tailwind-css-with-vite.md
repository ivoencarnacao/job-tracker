# ADR-0005: Tailwind CSS with Vite

## Status

Accepted

## Context

The server-rendered Thymeleaf templates need styling. The main options are:

1. **Bootstrap** — component-based CSS framework with predefined UI components.
2. **Plain CSS / SCSS** — custom stylesheets written from scratch.
3. **Tailwind CSS** — utility-first CSS framework that generates only the classes used.

A CSS build tool is needed to process Tailwind's utility generation. Options are Vite, Webpack, or PostCSS CLI.

## Decision

Use **Tailwind CSS 4** for styling with **Vite** as the CSS build tool.

## Consequences

### Positive

- **Utility-first approach** — styles are co-located with HTML in Thymeleaf templates. No context-switching between template and stylesheet files.
- **Design system alignment** — Tailwind's configuration maps directly to design tokens (spacing, colors, shadows, fonts), making the Neo-Brutalism design guidelines easy to implement as a custom theme.
- **Minimal CSS output** — Tailwind's JIT engine generates only the classes actually used in templates, resulting in small production CSS bundles.
- **Vite provides fast HMR** — CSS changes during development are reflected instantly without full page reloads, improving the development experience.
- **No component lock-in** — unlike Bootstrap, Tailwind does not impose component structure or JavaScript dependencies. HTML structure is fully controlled.

### Negative

- **Verbose HTML** — Thymeleaf templates accumulate many utility classes, reducing readability (`class="border-2 border-black shadow-[4px_4px_0_#000] p-4 font-bold"`).
- **Separate build step** — requires Node.js and Vite running alongside the Spring Boot process during development. Two terminals needed.
- **Tailwind learning curve** — developers need to learn utility class names and Tailwind's configuration system.
- **frontend-maven-plugin** — Node.js integration in Maven adds build complexity and a Node.js installation managed by the build tool.

### Mitigation

- Tailwind's `@apply` directive can extract repeated utility patterns into named classes when HTML becomes too verbose.
- The `frontend-maven-plugin` handles Node.js installation automatically — developers don't need to manage it manually.
- Vite's dev server is configured to work alongside Spring Boot's DevTools for a smooth development workflow.
