# ADR-0003: MapStruct for DTO Mapping

## Status

Accepted

## Context

The architecture separates domain entities from DTOs at multiple boundaries (application layer input/output DTOs, web layer request/response DTOs, view models). This requires mapping code between these representations.

Options considered:

1. **MapStruct** — compile-time code generation for type-safe mappings.
2. **ModelMapper** — runtime reflection-based mapping.
3. **Manual mapping** — hand-written conversion methods.

## Decision

Use **MapStruct 1.6.x** for DTO-to-entity and entity-to-DTO conversions.

## Consequences

### Positive

- **Compile-time safety** — mapping errors are caught at build time, not at runtime. Missing fields or type mismatches cause compilation failures.
- **Zero runtime overhead** — generates plain Java method calls; no reflection, no proxy objects, no runtime mapping configuration.
- **IDE support** — generated code is visible in the IDE, making debugging straightforward (just step into the generated mapper).
- **Spring integration** — mappers are Spring beans (`@Mapper(componentModel = "spring")`), injectable like any other service.
- **Explicit mappings** — when field names differ, the mapping is declared explicitly in annotations, serving as documentation.

### Negative

- **Annotation processing complexity** — requires annotation processor configuration in the Maven build. Can conflict with other annotation processors (e.g., Lombok) if not ordered correctly.
- **Learning curve** — developers unfamiliar with MapStruct need to learn the annotation API and understand the generated code.
- **Verbose for simple cases** — a mapper interface with `@Mapping` annotations can feel heavy for straightforward 1:1 field copies.

### Mitigation

- The Maven build already includes MapStruct's annotation processor in the correct order.
- For simple 1:1 mappings, MapStruct infers them automatically — only divergent field names need explicit `@Mapping` annotations.
