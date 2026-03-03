# ADR-0009: GitHub Actions for CI/CD

## Status

Accepted

## Context

The project needs automated build verification and deployment. The main options are:

1. **GitHub Actions** — CI/CD built into GitHub with YAML-based workflows.
2. **Jenkins** — self-hosted, highly customizable CI/CD server.
3. **CircleCI** — cloud-based CI/CD with Docker support.
4. **GitLab CI** — integrated CI/CD for GitLab repositories.
5. **Travis CI** — cloud-based CI with GitHub integration.

Key requirements: tight GitHub integration, zero infrastructure to manage, Maven build support, and the ability to trigger external deploy hooks.

## Decision

Use **GitHub Actions** with two separate workflows:

1. **Continuous Integration** (`build.yml`) — runs `./mvnw -B verify` on every push and pull request. Triggers on `main`, `develop`, `feature/**`, `release/**`, and `hotfix/**` branches. Skips runs when only Markdown files change. Uses concurrency groups to cancel in-progress runs when new commits are pushed.
2. **Continuous Delivery** (`deploy.yml`) — triggers a Render deploy hook via `curl` after the CI workflow completes successfully on `main`. Uses a `workflow_run` trigger to decouple CI from CD.

## Consequences

### Positive

- **Zero infrastructure** — no CI/CD servers to provision, maintain, or scale. GitHub hosts the runners.
- **Native GitHub integration** — status checks on pull requests, branch protection rules, and secrets management are built in.
- **Concurrency control** — `concurrency` groups with `cancel-in-progress: true` prevent redundant builds when commits are pushed in quick succession.
- **Maven dependency caching** — `setup-java` with `cache: "maven"` speeds up builds by caching the `.m2/repository`.
- **CI/CD separation** — the deploy workflow only runs after CI succeeds on `main`, ensuring broken code is never deployed. The two workflows are independently auditable.
- **Minimal workflow logic** — the build logic lives in Maven (`./mvnw verify`), not in the workflow YAML. The workflows are thin orchestration layers.

### Negative

- **GitHub vendor lock-in** — workflows use GitHub-specific syntax (`on`, `runs-on`, `uses`) and are not portable to other CI/CD platforms without rewriting.
- **Shared runners** — build times on `ubuntu-latest` runners can vary depending on GitHub's infrastructure load.
- **Secret management** — the Render deploy hook URL is stored as a GitHub secret (`RENDER_DEPLOY_HOOK`), requiring manual setup per repository.

### Mitigation

- Build logic is entirely in Maven, so migrating to another CI/CD platform only requires rewriting the thin workflow YAML, not the build process itself.
- Shared runner variability is acceptable for an MVP. Self-hosted runners can be added later if build times become critical.
