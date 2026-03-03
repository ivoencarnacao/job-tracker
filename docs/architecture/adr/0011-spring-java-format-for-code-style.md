# ADR-0011: Spring Java Format for Code Style

## Status

Accepted

## Context

Consistent code formatting reduces cognitive load during code reviews and prevents formatting-only diffs from polluting commit history. The main options are:

1. **Spring Java Format** — opinionated formatter aligned with Spring Framework conventions.
2. **Google Java Format** — opinionated formatter following Google's Java style guide.
3. **Checkstyle** — configurable style checker with extensive rule sets.
4. **Spotless** — multi-language formatting plugin supporting various formatters.
5. **No enforcement** — rely on IDE settings and developer discipline.

## Decision

Use **Spring Java Format 0.0.47** with its Maven plugin for automatic validation and formatting. The plugin runs in two modes:

- **`spring-javaformat:validate`** — bound to the `validate` phase, runs on every build. Fails the build if any Java file violates the format, ensuring violations are caught in CI before merge.
- **`spring-javaformat:apply`** — manual command that auto-fixes all formatting violations across the codebase.

## Consequences

### Positive

- **Zero configuration** — Spring Java Format is fully opinionated. No style rules to define, debate, or maintain. It enforces one consistent style across all Java files.
- **Build-integrated validation** — formatting violations fail the build during `./mvnw verify`, which runs in CI (GitHub Actions). Unformatted code cannot be merged.
- **One-command fix** — `./mvnw spring-javaformat:apply` corrects all violations automatically. Developers do not need to manually fix formatting issues.
- **Spring ecosystem alignment** — the format matches the style used in Spring Framework, Spring Boot, and Spring Data source code, making the codebase feel familiar to Spring developers.

### Negative

- **No customization** — the format is take-it-or-leave-it. Individual rules (brace placement, line length, import ordering) cannot be configured.
- **IDE conflicts** — IDE auto-formatters (IntelliJ, VS Code) may produce different output than Spring Java Format, causing violations after saving a file.
- **Build-time cost** — the validation step adds a few seconds to every build.

### Mitigation

- IntelliJ IDEA and Eclipse plugins are available to align IDE formatting with Spring Java Format, eliminating conflicts on save.
- Running `./mvnw spring-javaformat:apply` before committing ensures compliance regardless of IDE settings.
- The validation step is fast (typically under 2 seconds) and runs early in the build lifecycle, failing quickly if there are violations.
