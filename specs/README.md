# Requirements-Driven Development

No code without a requirement. A spec is not a planning document: it is the
list of guarantees the back-end owes, and each one is verifiable by a test.

There are two files per capability:

```
specs/
  CAPABILITIES.md        # capability → spec map, read by the audit
  SYSTEM.md              # transversal requirements (SYS01...), written once
  tasks-system.md        # work blocks for the SYS requirements
  _template/
  NNN-<capability>/
    spec.md              # requirements + API contract + data model
    tasks.md             # one block per requirement, closes with Validado
```

There is no `plan.md`. `tasks.md` is the plan: the order of its blocks is the
order of the work, and the checked boxes are the record of what landed.

## Requirement IDs

- Capability prefix plus two digits: `TAG01`, `MOV01`, `CAT01`, `AUTH01`.
- Transversal: `SYS01`, `SYS02`... in `SYSTEM.md`.
- An ID is written once. Never renumber it, never reuse it. If a requirement
  is dropped, strike it through and keep the number reserved.
- One requirement is one verifiable sentence in business language. If it
  needs another rule, it is another ID.

## Capabilities

`CAPABILITIES.md` lists every capability and the spec that covers it. The audit
reports entries marked `missing` on every build. Adding a capability means
adding a row; creating a spec folder that is not listed fails the audit.

## `spec.md`

```markdown
# <Capability>

<2-3 lines of context: who uses it and what problem it solves.>

## Requirements

- **TAG01** — <guarantee the system must provide, in business language.>

## API Contract

| Method | Path | Req | Success | Errors |
|--------|------|-----|---------|--------|
| `POST` | `/api/tags` | TAG01, TAG02 | `201` | `400`, `401`, `409` |

## Data Model

<Tables, columns, constraints, value objects and their invariants.>

## Out of Scope

<What was deliberately excluded. Keep it short.>
```

No dates, authors, versions, priorities or status fields. The requirement
list is the bulk of the file; the rest is the technical contract needed to
build it.

## `tasks.md`

One block per requirement, in execution order:

```markdown
## TAG02 — Name is unique per user
- [x] Normalise the name for comparison (`TagName`, domain)
- [x] Reject duplicates in `CreateTagUseCase` (application)
- [x] Unique index `(user_id, lower(name))` (Liquibase)
- [x] Map `DataIntegrityViolation` to 409 (infrastructure)
- [x] **Validado** — `tag02_*` (5 tests)
```

Rules:

- **`Validado` is the last line of every block.** The requirement is not done
  while that box is unchecked.
- It may only be checked when every box in the block is checked **and** the
  tests that reference the ID pass.
- If a requirement changes or one of its tests goes red, uncheck `Validado`
  before doing anything else. The diff is the record of the change.
- Sub-items are work, not requirements. Keep them short and name the layer.

## Tests

A test that verifies a requirement starts its name with the lowercase ID:

```java
void tag02_rejectsDuplicateNameForSameUser()
```

Several tests may cover one requirement, in any layer. See
`docs/testing.md`. `grep -ri tag02` returns the tests and, from there, the
implementation the requirement depends on.

## Definition of done

- A requirement: every box in its block checked plus `Validado` with evidence.
- A capability: every ID in its `spec.md` has `Validado`.

## Audit

```bash
./gradlew requirementsCheck
```

Read-only. Cross-checks requirement IDs against the Gradle test results and
fails when: an ID has no block in `tasks.md`, a block does not match any
requirement, a block is `Validado` with unchecked boxes, or `Validado` has no
passing tests. It also lists the requirements that are still pending. It runs
as part of `check`.

## Splitting

Split a capability into numbered slices when it has independent deliverables
or more than ~15 requirements. Slices are vertical (shippable end to end),
never by layer. A parent `README.md` lists the slices and their order.

## Legacy

A spec without requirement IDs is skipped by the audit. There are none left in
this repository; new specs must use IDs from the start.
