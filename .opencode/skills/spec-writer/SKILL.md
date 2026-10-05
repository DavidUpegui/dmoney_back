---
name: spec-writer
description: Use when writing, revising, or scaffolding Spec-Driven Development specs (spec.md, plan.md, tasks.md) for the dmoney project. Covers spec structure, vertical-slice splitting, change management, interactive requirement gathering, and scaffolding the specs/ directory.
---

# Spec-Writer (SDD)

Write and maintain Spec-Driven Development specs for the `dmoney` backend. The
rule is **spec-first**: no code changes until the spec reflects the new truth.

## Before anything else

1. Read `AGENTS.md` (always in context) to load the non-negotiable rules:
   spec-first, owner-scoping (`UserId`), Liquibase, JaCoCo ≥ 80%, self-validating
   value objects.
2. Read `docs/architecture.md` and `docs/testing.md` (available as `@docs`)
   when the feature spans layers or needs test-planning.
3. If `specs/` does not exist, scaffold it (see "Scaffolding").

## Interactive questioning protocol

ALWAYS gather requirements by asking questions — never assume. Run the full
protocol and challenge every spec before writing it. Use the `question` tool
for choices (2–4 at a time, with recommended option first) and plain text for
open questions. Cover:

1. **Scope** — Is this one feature or several? Should it be split into
   vertical slices now (e.g. CRUD vs filter)? What's explicitly out of scope?
2. **Actors & ownership** — Who uses it? Is every operation scoped to the
   current `UserId`? What happens for a missing JWT (401) vs a foreign
   resource (404)?
3. **API contract** — For each endpoint: verb, path, auth, request/response
   shape, and status codes (200/201/204/400/401/404). Confirm the error body
   format matches `ApiError`/`GlobalExceptionHandler`.
4. **Data model** — Entities, fields, constraints, and relationships. For each
   value object: what makes it invalid? (null, blank, negative, empty…)
5. **Business rules** — Invariants and edge cases. Always probe: "what happens
   when X is null?", "is that a 400 or 404?", "does this belong to the current
   user?", "is empty allowed?", "case sensitivity?"
6. **Change impact** — Is this new, additive, behavioral, or breaking? Does it
   touch Liquibase (new changeset) or existing tables?
7. **Unknowns** — Anything the user can't answer yet becomes an explicit
   `draft` note or "Out of Scope" entry.

Challenge vague wording. If an acceptance criterion can't be written as
Given/When/Then, ask for clarification.

## Scaffolding

On first use (or if any piece is missing), create:

```
specs/
  README.md              # SDD conventions & workflow (see template below)
  _template/
    spec.md
    plan.md
    tasks.md
  001-<feature>/         # one folder per feature, numbered in build order
```

Also ensure `AGENTS.md` and `docs/` exist; if absent, propose generating them.

`specs/README.md` must state: spec-first rule, directory structure, workflow
(spec → plan → implement → test → verify), status markers, and "split by
vertical slice, never by layer".

## Templates

### `spec.md`

```markdown
# Feature: <Feature Name>

- **Status:** `draft`
- **Folder:** `NNN-<feature-name>`
- **Related module:** `tracker` | `auth`

## 1. Overview
What this feature does, who the actors are, what problem it solves.

## 2. Requirements
### Functional (FR)
- **FR-1** — ...
### Non-functional (NFR)
- **NFR-1** — Security/performance/validation (global rules from AGENTS.md apply).

## 3. API Contract
| Method | Path | Description | Auth |
|--------|------|-------------|------|
| `POST` | `/api/...` | Create | required |

Request/response JSON examples and a status-code table (200/201/204/400/401/404).

## 4. Data Model
Entities, fields, constraints; value objects with their invariants.

## 5. Business Rules & Validation
Testable statements; ownership is always scoped by `UserId`.

## 6. Edge Cases
Nulls, duplicates, missing references, cross-user access, case sensitivity.

## 7. Acceptance Criteria
- **AC-1** — Given X, when Y, then Z.  (each maps to a test)

## 8. Out of Scope
Explicitly deferred items.
```

### `plan.md`

```markdown
# Plan: <Feature Name>

## Domain layer (`.../domain/...`)
- [ ] Aggregate + value objects
- [ ] Repository interface changes
- [ ] Domain service / validator

## Application layer (`.../application/...`)
- [ ] Commands / responses
- [ ] Use cases

## Infrastructure layer (`.../infrastructure/...`)
- [ ] Controller + DTOs + web mapper
- [ ] JPA entity + adapter + persistence mapper
- [ ] Liquibase changelog (if schema changes)

## Tests (`src/test/...`)
- [ ] Value object tests
- [ ] Use case tests
- [ ] Controller / integration tests
- [ ] Coverage ≥ 80%
```

### `tasks.md`

```markdown
# Tasks: <Feature Name>

## Spec
- [ ] `spec.md` written and reviewed
## Implementation
- [ ] Domain / Application / Infrastructure done
## Tests
- [ ] Value object / use case / controller tests
## Verification
- [ ] `./gradlew clean build` passes
- [ ] Coverage ≥ 80%
```

## Workflow

1. Scaffold if needed, then run the questioning protocol.
2. Draft `spec.md`; present it to the user and refine until they confirm.
3. Derive `plan.md` and `tasks.md` from the spec.
4. Report the status marker and the next step (implementation).

## Change management

- Always edit the spec first, then the code. Reconcile both in the same change.
- Classify changes: **additive** (append FR/AC), **behavioral** (update rule +
  AC, update tests), **breaking** (bump the API contract, decide migration).
- Add a short changelog entry to the spec when a change is non-trivial.
- Schema changes → new Liquibase changeset; never edit an applied one.
- **New global rule surfaced?** Propose an update to `AGENTS.md` (or the docs)
  rather than duplicating it across feature specs.
- **Cross-cutting "not now" issue surfaced?** Record a dated one-liner in
  `OBSERVATIONS.md` and move on; delete it once resolved. Feature-specific
  "not now" items go in the spec's "Out of Scope" instead.

## Splitting a large spec

Split when: >~15–20 acceptance criteria, multiple independent deliverables,
one change touches many unrelated sections, or parallel work is possible.

- Split by **vertical slice** (shippable end-to-end), never by layer.
- Each slice keeps its own ACs + API contract; a parent `README.md` ties them
  together and states dependencies/order.
- Global rules stay in `AGENTS.md`; don't copy them into every spec.

## Status markers

`draft` → `in-progress` → `implemented`, stated at the top of each `spec.md`.
