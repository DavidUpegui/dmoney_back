---
name: dmoney-delivery
description: Use when implementing or validating requirements that already exist in specs/ for dmoney_back — phrases like "implementa TAG02", "sigamos con tags", "valida lo que llevamos", "cierra la feature", "corre los tests". Runs the tasks.md block for one requirement at a time with TDD and only marks Validado with green tests.
---

# dmoney-delivery

Implement requirements that are already written. One requirement at a time,
test first, `Validado` only with evidence.

## Preconditions

- The requirement exists in `specs/<capability>/spec.md` (or `SYSTEM.md`).
- Its block exists in the sibling `tasks.md` (`specs/tasks-system.md` for
  `SYS`). If it is missing, create it at the right position, following
  `specs/_template/tasks.md` — the order of the blocks is the plan.
- If the requirement does not exist, stop: this is `dmoney-requirements`
  work. Do not invent an ID mid-implementation.

## Loop, per sub-item

1. Write the failing test first. Its name starts with the lowercase ID:

   ```java
   void tag02_rejectsDuplicateNameForSameUser()
   ```

   Put it in the layer where the behaviour lives (`domain` value object,
   `application` use case with mocked ports, `infrastructure` controller or
   adapter). Several tests per requirement, in different layers, is expected.
   Do not add a `@DisplayName` that hides the ID: the audit reads the name
   Gradle reports.
2. Run it and confirm it fails for the right reason.
3. Implement the minimum that makes it pass, respecting the hexagonal
   boundaries in `docs/architecture.md`.
4. Run it again. Refactor only while green.
5. Tick the sub-item in `tasks.md`.

Do not jump to the next requirement with a red test.

## Closing a block

The requirement is done only when:

- Every sub-item in the block is checked.
- The tests whose names start with the ID pass.

Then add or update the last line of the block:

```markdown
- [x] **Validado** — `tag02_*` (5 tests)
```

Run the tests before writing that line. Never mark `Validado` optimistically,
never mark it without running the suite.

## Contract sync

If the change touches an endpoint, path, status code or field name, update
`../dmoney_platform/contracts/API_CONTRACT.md` and `docs/api.md` in the same
change set, keeping the `Req` column accurate.

## Verification

- Per requirement: `./gradlew test --tests "*<Class>Test"` while iterating,
  then the full `./gradlew test`.
- Per capability or slice: `./gradlew clean build` (coverage ≥ 80% via
  JaCoCo) and `./gradlew requirementsCheck`.
- `./gradlew requirementsCheck` fails when an ID has no block, a block has no
  requirement, `Validado` has unchecked sub-items, or `Validado` has no
  passing test. Fix the cause, never the check.

## If something changes or breaks

- A test for a validated requirement goes red → uncheck `Validado`
  immediately, then fix or hand back to `dmoney-requirements` if the
  requirement itself changed.
- A requirement changes → the `Validado` uncheck and the new sub-items come
  from `dmoney-requirements`. Do not edit the statement here.
- A cross-cutting issue appears that is out of scope → one dated line in
  `OBSERVATIONS.md`, then continue.

## Rules

- Never write a test without a requirement ID unless it tests a pure
  technical helper.
- Never assert implementation details; assert observable behaviour.
- Never create `plan.md`, status fields or changelog sections.
- Never leave the tree with a red test and a checked `Validado`.
