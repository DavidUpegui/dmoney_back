# Movement — CRUD

A movement records a financial transaction for a user: an amount classified by
a category/subcategory, optionally tagged, with a type, a description and a
date. This slice covers create, list, find, update and delete.

## Requirements

- **MOV01** — A user can create a movement with valid data; the response is
  `201` with a `Location` header and the created movement.
- **MOV02** — The amount must be strictly greater than zero; zero or negative
  returns `400`.
- **MOV03** — The type is `INCOME`, `OUTCOME` or `TRANSFER`, case-insensitive on
  input, and stored uppercase.
- **MOV04** — The description is optional, at most 500 characters, trimmed;
  `null` becomes an empty string.
- **MOV05** — The category must belong to the authenticated user; a foreign
  category returns `404`.
- **MOV06** — The subcategory must belong to the movement's category;
  otherwise the request returns `400`.
- **MOV07** — Every tag must belong to the authenticated user; a foreign tag
  returns `404`.
- **MOV08** — A user can list only their own movements.
- **MOV09** — A user can read one of their movements by id; a missing or
  foreign id returns `404`.
- **MOV10** — A movement can be updated partially: only the fields present
  change, `tagIds: []` clears the tags, and ownership is re-validated when
  category, subcategory or tags change.
- **MOV11** — A user can delete one of their movements; the response is `204`
  and a missing or foreign id returns `404`.

## API Contract

| Method | Path | Req | Success | Errors |
|--------|------|-----|---------|--------|
| `POST` | `/api/movements` | MOV01–MOV07 | `201` + `Location` | `400`, `401`, `404` |
| `GET` | `/api/movements` | MOV08 | `200` | `401` |
| `GET` | `/api/movements/{id}` | MOV09 | `200` | `401`, `404` |
| `PATCH` | `/api/movements/{id}` | MOV05–MOV07, MOV10 | `200` | `400`, `401`, `404` |
| `DELETE` | `/api/movements/{id}` | MOV11 | `204` | `401`, `404` |

### Request / Response

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

`PATCH` accepts any subset of the same fields. `tagIds: []` clears the tags;
omitted or `null` fields keep their current value.

## Data Model

| Entity | Fields | Constraints |
|--------|--------|-------------|
| `movement` | `id`, `user_id`, `cat_id`, `subcat_id`, `amount`, `type`, `description`, `date` | `id` UUID PK; `amount` NUMERIC(19,4) > 0; `type` enum string; `description` max 500 |
| `movement_tags` | `movement_id`, `tag_id` | composite PK; FK to `movement` with `ON DELETE CASCADE` |

Value objects: `MovementId`, `Amount` (> 0), `MovementType` (case-insensitive
parse), `MovementDescription` (trimmed, max 500, null → empty).

## Out of Scope

- Filtering and search; that is `002-filter`.
- Pagination.
- Ordering of the list; no order is guaranteed.
- Enriching the response with category/subcategory/tag names.
- Currency handling (single currency assumed).
- What happens to `movement_tags` when a tag is deleted (see `OBSERVATIONS.md`).
