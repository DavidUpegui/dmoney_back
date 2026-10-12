# Feature: Movement — CRUD

- **Status:** `implemented`
- **Folder:** `001-movement/001-crud`
- **Related module:** `tracker`

## 1. Overview

A movement records a financial transaction for a user: an amount classified by
a category/subcategory, optionally tagged, with a type (INCOME / OUTCOME /
TRANSFER), a description, and a date. This slice covers the full CRUD
lifecycle. Create and list already exist and are documented here as the
baseline; find-by-id, update, and delete are new.

## 2. Requirements

### Functional (FR)

- **FR-1** — Create a movement.
- **FR-2** — List all movements for the current user.
- **FR-3** — Find a movement by id.
- **FR-4** — Update a movement (partial).
- **FR-5** — Delete a movement.

### Non-functional (NFR)

- **NFR-1** — Every operation is scoped to the current `UserId`.
- **NFR-2** — Validation error → `400`; foreign/missing resource → `404`; no token → `401`.
- **NFR-3** — Coverage ≥ 80%, one test per acceptance criterion.

## 3. API Contract

| Method   | Path                    | Description | Auth |
|----------|-------------------------|-------------|------|
| `POST`   | `/api/movements`        | Create      | required |
| `GET`    | `/api/movements`        | List        | required |
| `GET`    | `/api/movements/{id}`   | Find        | required |
| `PATCH`  | `/api/movements/{id}`   | Update      | required |
| `DELETE` | `/api/movements/{id}`   | Delete      | required |

### Create

```json
POST /api/movements
{
  "categoryId": "20000000-...",
  "subcategoryId": "30000000-...",
  "amount": 85.50,
  "type": "OUTCOME",
  "description": "Arriendo",
  "date": "2026-07-14",
  "tagIds": ["40000000-..."]
}
```

- `amount` is `BigDecimal > 0`.
- `type` is `INCOME | OUTCOME | TRANSFER`, case-insensitive on input.
- `description` may be null or empty.
- `tagIds` is optional.

`201` → `MovementResponse` + `Location` header.

### Update (PATCH)

Only provided fields change; null fields are left unchanged. Re-validates
category/subcategory/tag ownership when those fields change.

### Response shape

```json
{
  "movementId": "50000000-...",
  "categoryId": "20000000-...",
  "subcategoryId": "30000000-...",
  "amount": 85.50,
  "type": "OUTCOME",
  "description": "Arriendo",
  "date": "2026-07-14",
  "tagIds": ["40000000-..."]
}
```

### Status codes

- `200` — find / list / update
- `201` — create
- `204` — delete
- `400` — validation error
- `401` — unauthenticated
- `404` — movement, category, subcategory, or tag not found

## 4. Data Model

| Entity          | Fields | Constraints |
|-----------------|--------|-------------|
| `movement`      | `id`, `user_id`, `cat_id`, `subcat_id`, `amount`, `type`, `description`, `date` | `id` UUID PK; `amount` `NUMERIC(19,4) > 0`; `type` enum string |
| `movement_tags` | `movement_id`, `tag_id` | composite PK; FK cascade on `movement_id` |

Value objects (immutable, self-validating):

- `MovementId` — non-null, non-blank UUID.
- `Amount` — non-null `BigDecimal`, must be `> 0`.
- `MovementType` — `INCOME | OUTCOME | TRANSFER`, parsed case-insensitively.
- `MovementDescription` — non-null, trimmed; empty allowed.

## 5. Business Rules & Validation

- The movement belongs to the current `UserId`.
- The category must belong to the user.
- The subcategory must belong to the category.
- Every tag must belong to the user.
- `type` is case-insensitive on input (rule to fix current behavior).
- `description` may be empty but not null (rule to fix current behavior).

## 6. Edge Cases

- Negative/zero amount → `400`.
- Lowercase `type` → accepted.
- Null `description` → treated as empty (not a `500`).
- Foreign category/tag → `404`; subcategory not in category → validation error.
- Find/update/delete a non-existent or foreign movement → `404`.

## 7. Acceptance Criteria

- **AC-1** — Given valid input, when create, then `201` + `Location` + persisted.
- **AC-2** — Given amount ≤ 0, when create, then `400`.
- **AC-3** — Given a lowercase `type`, when create, then accepted.
- **AC-4** — Given null/empty `description`, when create, then accepted (empty).
- **AC-5** — Given a category belonging to another user, when create, then `404`.
- **AC-6** — Given a subcategory not in the category, when create, then validation error.
- **AC-7** — Given a tag belonging to another user, when create, then `404`.
- **AC-8** — When list, then only the current user's movements are returned.
- **AC-9** — Given an id, when find, then the owner's movement; foreign/unknown → `404`.
- **AC-10** — Given an id + changed fields, when patch, then updated; unchanged nulls preserved.
- **AC-11** — Given an id, when delete, then `204`; foreign/unknown → `404`.
- **AC-12** — Given no token, any endpoint → `401`.

## 8. Out of Scope

- Filtering/searching (`002-filter`).
- Pagination.
- Enriching the response with category/subcategory/tag names.
- Currency handling (single currency assumed).

## Changelog

- `2026-10-05` — Retroactive baseline for create/list; documented the
  `type` case-insensitivity and nullable-`description` fixes as explicit rules.
- `2026-10-08` — Renamed the create/update request field `tags` → `tagIds` to
  match the response and filter parameter (`tagIds`), resolving the API
  asymmetry.
