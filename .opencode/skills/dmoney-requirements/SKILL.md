---
name: dmoney-requirements
description: Use when eliciting, writing or changing the requirements of a dmoney_back capability — phrases like "levantemos los requisitos de tags", "qué debe garantizar el back en tags", "agreguemos un campo a tags", "cambió el requisito de tags", "qué pasa si...". Writes spec.md and unchecks Validado in tasks.md; never writes code or tests.
---

# dmoney-requirements

Turn what the business wants into a list of numbered, verifiable guarantees.
Two modes: **elicit** (new capability or new requirement) and **change** (an
existing requirement moves). Never write code in either mode.

## Read first

`AGENTS.md`, `specs/README.md`, `specs/_template/spec.md`,
`specs/SYSTEM.md`, the capability's `spec.md`/`tasks.md` if it exists, and
`../dmoney_platform/contracts/API_CONTRACT.md`. Use `dmoney-context` first if
you do not know the area yet.

## Mode: elicit

Ask before writing. One round of questions at a time; use the `question` tool
for choices and plain text for open ones. Never fill a gap with an assumption
— an unanswered question becomes an explicit "Out of Scope" line or stays a
question for the user.

Probe, per capability:

1. **Actor and ownership** — who uses it? Is every operation scoped to the
   authenticated `UserId`? Missing JWT → `401`; foreign resource → `404`.
2. **Shape** — which fields, which are required, lengths, formats, defaults.
   What is invalid, and what does the client see?
3. **Uniqueness and normalisation** — case, whitespace, accents, per user or
   global, what happens on duplicate (`409`).
4. **Limits and empties** — empty strings, zero, negative, null, maximums.
5. **Lifecycle** — create, update, delete; what happens to related data on
   delete; what is immutable.
6. **HTTP surface** — verb, path, success code, error codes, and the error
   body (`ApiError`). Which endpoint serves which requirement.
7. **Classification** — business requirement, transversal `SYS`
   requirement, or build convention (coverage, Liquibase, value objects) that
   belongs in `AGENTS.md`/`docs/` and is **not** a requirement.
8. **Side** — is this guaranteed by the back-end, the front-end, or both? If
   both, the shared part belongs in `contracts/` with the same ID.
9. **Out of scope** — what is deliberately not included now.

Challenge vague answers. If a sentence cannot be tested, it is not a
requirement yet. Keep asking.

## Write the spec

Follow `specs/_template/spec.md` and `specs/README.md`. The shape is fixed:

- `# <Capability>` + 2–3 lines of context.
- `## Requirements` — the bulk of the file, one line per ID.
- `## API Contract` — table with a `Req` column mapping endpoints to IDs.
- `## Data Model` — tables, constraints, value objects and invariants.
- `## Out of Scope` — short.

Rules:

- IDs are `<CAPABILITY><NN>` (`TAG01`) for the capability and `SYS<NN>` for
  transversal guarantees in `specs/SYSTEM.md`.
- IDs are immutable and never reused. A dropped requirement is struck
  through, its number stays reserved.
- One requirement is one verifiable sentence in business language. A second
  rule is a second ID.
- No dates, authors, versions, priorities or status fields. No
  acceptance-criteria section: the requirement is the criterion.
- Do not create `plan.md`. Do not duplicate global rules from `AGENTS.md`.
- Create `tasks.md` only if it does not exist, using
  `specs/_template/tasks.md`.

When the API surface changes, update `contracts/API_CONTRACT.md` and
`docs/api.md` in the same change set.

## Mode: change

When a requirement moves, the goal is a bounded blast radius.

1. `grep -rn "<ID>" specs/ src/` and read the tests whose names start with the
   ID. Read the contract rows that reference it.
2. State the impact before editing: requirement text, tests, layers,
   endpoints, contract, `tasks.md` block.
3. Edit the statement in place, keeping the ID. If it is a new guarantee,
   add a new ID instead.
4. Uncheck `Validado` in `tasks.md`, add or remove sub-items, and leave the
   block representing the new work. Do not implement it.
5. If the requirement is dropped, strike it through and remove its block's
   `Validado`, keeping the ID reserved.

## Rules

- Never write production code or tests in this skill.
- Never check `Validado`.
- Never renumber an ID or reuse a retired one.
- If a "requirement" is really a design decision, record it in the spec's
  data model or in `AGENTS.md` as a convention — not as an ID.
- If the requirement is technical/internal but observable (an endpoint, a
  scheduled job, a response format), it still gets an ID. If it is a rule
  about how we build, it does not.

## Handoff

Present the requirement list and get explicit confirmation, then hand off to
`dmoney-delivery` for one ID at a time.
