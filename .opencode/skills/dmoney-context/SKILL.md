---
name: dmoney-context
description: Use at the start of any work on dmoney_back to build a bounded context map before proposing or writing anything — when the user says "vamos a trabajar en X", "cómo está implementado X", "explícame X", "retomemos X", or names a capability (tags, movements, categories, auth). Read-only; it never writes files and never proposes requirements.
---

# dmoney-context

Build the smallest context that lets you act correctly on `dmoney_back`. This
is step 0 of the workflow: context → requirements → delivery.

## Read, in this order

1. `AGENTS.md` (already in context) — architecture, non-negotiable rules.
2. `docs/architecture.md` — bounded contexts and layer responsibilities.
3. `docs/testing.md` — test layout and naming.
4. `specs/README.md` — the requirement model.
5. `specs/SYSTEM.md` and `specs/<NNN-capability>/spec.md`, if they exist —
   the requirements already written for this capability.
6. `../dmoney_platform/contracts/API_CONTRACT.md` and `docs/api.md` — the shared
   HTTP surface and the back's detailed reference.
7. `OBSERVATIONS.md` — known defects that may affect the work.

Then locate, with search rather than guessing:

- The bounded context (`auth`, `tracker`, `shared`) and the packages under
  `domain`, `application`, `infrastructure` that the capability touches.
- Existing endpoints, use cases, aggregates, value objects and repository
  ports that can be reused.
- Existing tests for the area (names, layers, coverage) and Liquibase
  changesets that own the relevant tables.
- Whether the capability has a spec and, if so, which requirement IDs are
  `Validado` and which are pending in `tasks.md`.

## Output

Answer in the chat, short and factual. No file edits.

```
Capacidad: <name> · bounded context: <auth|tracker|shared>
Spec: <path or "no existe"> · IDs: <TAG01..TAG0n | ninguno> · Validados: <n/m>
Código: <packages / classes worth reusing>
Endpoints: <existing routes this touches>
Tests: <where they live, what they cover>
Migraciones: <changesets that own the tables>
Contrato: <what API_CONTRACT says today>
Observaciones: <relevant OBSERVATIONS entries>
Huecos: <what is missing to do the work>
No sé: <open questions for the user>
```

## Rules

- Do not propose requirements, designs or implementations here. Reporting a
  gap is fine; deciding what to do about it belongs to `dmoney-requirements`.
- If the capability has no spec, say so explicitly: the next step is
  requirements, not code.
- If two bounded contexts seem involved, say which one owns the change.
- Keep the map under ~20 lines. Detail belongs in the files, not the answer.
- Never scaffold `specs/`, never create a spec from this skill.

## Handoff

- New or changed behaviour → `dmoney-requirements`.
- Requirements already written → `dmoney-delivery`.
