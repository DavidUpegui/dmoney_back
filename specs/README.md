# Spec-Driven Development (SDD)

This directory contains the specifications that drive the implementation of
the `dmoney` backend. The rule is simple: **no code without a spec first**.

## Structure

```
specs/
  README.md               # this file — conventions & workflow
  _template/              # reusable spec scaffold
    spec.md               # WHAT: requirements, API, data model, rules, acceptance
    plan.md               # HOW: implementation plan mapped to code layers
    tasks.md              # TRACKING: checklist of work items
  001-movement/           # one folder per feature, numbered in build order
    README.md             # index: slices + order/dependencies
    001-crud/             # vertical slices (independently shippable)
    002-filter/
```

Each feature gets a numbered folder (`NNN-kebab-case-name`). Features may
contain multiple numbered **slices** — vertical, independently shippable
pieces (e.g. CRUD vs filter). Numbers reflect build order, not priority.

## Workflow

1. **Spec** — write `spec.md` before touching code: *requirements*, *API
   contract*, *data model*, *business rules*, *edge cases*, and *acceptance
   criteria*.
2. **Plan** — break the spec into work items in `plan.md`, mapped to the
   hexagonal layers (domain → application → infrastructure).
3. **Implement** — work through `tasks.md`, ticking items off as they land.
4. **Test** — every acceptance criterion maps to at least one test (see
   `docs/testing.md`).
5. **Verify** — `./gradlew clean build` + coverage ≥ 80% before marking done.

## Golden rules

- The spec is the source of truth; if code and spec disagree, reconcile them
  explicitly (never leave them divergent).
- API contracts are written against the actual endpoint paths (`/api/...`).
- Business rules are stated as testable statements (Given/When/Then).
- Split by **vertical slice**, never by layer.
- Global rules (owner-scoping, security, Liquibase, testing) live in
  `AGENTS.md`/`docs/` and are **not** repeated in every spec.

## Status markers

| Status         | Meaning                                  |
|----------------|------------------------------------------|
| `draft`        | Spec in progress, not yet implemented     |
| `in-progress`  | Some tasks implemented, spec stable       |
| `implemented`  | All tasks done and verified               |

## Template

```bash
cp -r specs/_template specs/NNN-your-feature
```
