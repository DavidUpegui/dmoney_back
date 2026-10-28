# Feature: <Feature Name>

- **Status:** `draft`
- **Folder:** `NNN-<feature-name>`
- **Related module:** `tracker` | `auth`

## 1. Overview

A short paragraph describing what this feature does and why it exists. Who are
the actors, and what problem does it solve?

## 2. Requirements

### Functional (FR)

- **FR-1** — Description of a required capability.
- **FR-2** — ...

### Non-functional (NFR)

- **NFR-1** — Security: only the authenticated owner can access their resources.
- **NFR-2** — Performance / validation / etc.

## 3. API Contract

| Method | Path | Description | Auth |
|--------|------|-------------|------|
| `POST` | `/api/<resource>` | Create | required |
| `GET`  | `/api/<resource>` | List | required |

### Request / Response examples

```json
POST /api/<resource>
{ "field": "value" }
```

```json
201 Created
{ "id": "..." }
```

### Status codes

- `201` — created
- `400` — validation error
- `401` — unauthenticated
- `404` — resource not found

## 4. Data Model

| Entity | Fields | Constraints |
|--------|--------|-------------|
| `X`    | `id`, ... | `id` UUID PK, etc. |

Value objects (immutable, self-validating): list each with its invariants.

## 5. Business Rules & Validation

- Rules stated as testable statements.
- Ownership: every query/command is scoped by `UserId`.

## 6. Edge Cases

- Empty / null inputs.
- Duplicate names.
- Missing referenced resources.
- Case sensitivity.

## 7. Acceptance Criteria

Each criterion maps to a test.

- **AC-1** — Given X, when Y, then Z.

## 8. Out of Scope

- Things deliberately not included in this feature.
