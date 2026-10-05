# AGENTS.md

## Project

`dmoney` backend — a personal money tracker. Spring Boot 3.3.5, Java 21, Gradle, PostgreSQL + Liquibase, Spring Security + JWT (jjwt 0.12.5), SpringDoc OpenAPI, JaCoCo + SonarCloud.

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

- **Spec-first (SDD)**: write or update the spec in `specs/` before touching code. See `specs/README.md`.
- **Owner-scoping**: every query/command is scoped by `UserId`; never expose another user's data. Missing JWT → `401`; another user's resource → `404`.
- **Liquibase**: add a new changeset; never edit an already-applied one.
- **Tests**: package-by-layer under `src/test`, JaCoCo coverage ≥ 80%. See `docs/testing.md`.
- **Value objects** are immutable and self-validating (throw `ValidationException` on invalid input).
- **Observations**: when you detect a cross-cutting issue that's out of scope for the current task, record it in `OBSERVATIONS.md` and move on; delete the entry once resolved.

## Where things live

- Specs & SDD conventions: `specs/` (`specs/README.md`)
- Test conventions: `docs/testing.md`
- Architecture detail: `docs/architecture.md`
- Parked observations: `OBSERVATIONS.md`
