# AGENTS.md

## Project

`dmoney` backend — a personal money tracker. Spring Boot 3.3.5, Java 21, Gradle, PostgreSQL + Liquibase, Spring Security + JWT (jjwt 0.12.5), SpringDoc OpenAPI, JaCoCo + SonarCloud.

## Repository

- **Remote**: `git@github.com:DavidUpegui/dmoney_back.git` (SSH)
- **Branching**: Trunk-based development; default branch is `trunk`.
- **Git workflow**: `../dmoney_platform/docs/technical/git-workflow.md` —
  branch names, commit format, PR rules and merge strategy.

## Architecture

Hexagonal, three bounded contexts, each split into `domain` / `application` / `infrastructure`:

- `auth` — users, registration/login, JWT generation/parsing, BCrypt hashing.
- `tracker` — categories (with subcategories), tags, movements.
- `shared` — `UserId`, domain exceptions, global exception handler.

Layer responsibilities:
- `domain` — aggregates, immutable self-validating value objects, repository interfaces, domain services.
- `application` — commands/responses (records), use cases.
- `infrastructure` — REST controllers + DTOs, JPA entities + adapters/mappers, Liquibase.

## Non-negotiable rules

- **Requirements-first**: no code without a requirement. Every behaviour lives as a numbered requirement (`TAG01`, `SYS01`) in `specs/` before it is implemented. See `specs/README.md`.
- **`Validado`**: each requirement has a block in `tasks.md`; it is not done until the block ends with `- [x] **Validado**`, which requires every sub-item checked and its tests green. Uncheck it when the requirement or its tests change.
- **Test naming**: a test that verifies a requirement starts with the lowercase ID (`tag02_...`). See `docs/testing.md`.
- **Owner-scoping**: every query/command is scoped by `UserId`; never expose another user's data. Missing JWT → `401`; another user's resource → `404`.
- **Liquibase**: add a new changeset; never edit an already-applied one.
- **Tests**: package-by-layer under `src/test`, JaCoCo coverage ≥ 80% (enforced by `check`). See `docs/testing.md`.
- **Value objects** are immutable and self-validating (throw `ValidationException` on invalid input). Known exception: the ID value objects, tracked in `OBSERVATIONS.md`.
- **Observations**: when you detect a cross-cutting issue that's out of scope for the current task, record it in `OBSERVATIONS.md` and move on; delete the entry once resolved.

## Workflow

Three phases, three skills:

1. **Context** (`dmoney-context`) — read the area, report what exists. No writes.
2. **Requirements** (`dmoney-requirements`) — elicit or change requirement IDs in `spec.md`. No code.
3. **Delivery** (`dmoney-delivery`) — one ID at a time, TDD, `Validado` with green tests.

If it is unclear which phase the request belongs to, ask before acting.

## Where things live

- Requirements model & conventions: `specs/README.md`
- Transversal requirements: `specs/SYSTEM.md` and `specs/tasks-system.md`
- Requirement audit: `./gradlew requirementsCheck` (runs as part of `check`)
- Test conventions: `docs/testing.md`
- Architecture detail: `docs/architecture.md`
- Parked observations: `OBSERVATIONS.md`

## Running locally

`application.yaml` holds shared configuration only. The database connection and
the `dev` seed data live in the `local` profile:

```bash
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

`dmoney_platform/deploy/local/docker-compose.yml` sets the profile for you.
