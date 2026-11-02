# Movement — Filter & Search

Extends `GET /api/movements` with optional filtering and text search.
Multi-select is supported for categories, subcategories and tags. All
dimensions combine with **AND** semantics; omitting all of them returns the
current user's full movement list.

## Requirements

- **MOV12** — Filter by one or more categories; within the selection the match
  is OR.
- **MOV13** — Filter by one or more subcategories; within the selection the
  match is OR.
- **MOV14** — Filter by one or more tags; a movement matches when it has any of
  them.
- **MOV15** — Filter by type, case-insensitive.
- **MOV16** — Filter by an inclusive `dateFrom` / `dateTo` range.
- **MOV17** — Search by description, case-insensitive and trimmed; a blank
  search is ignored.
- **MOV18** — Any combination of dimensions is AND; with no parameters the
  response is all of the user's movements.
- **MOV19** — A malformed UUID or type in the filter returns `400`.
- **MOV20** — Filters never return another user's movements.

## API Contract

### `GET /api/movements`

| Param | Type | Req | Meaning |
|-------|------|-----|---------|
| `categoryIds` | UUID list (comma-separated) | MOV12 | filter by category (OR) |
| `subcategoryIds` | UUID list (comma-separated) | MOV13 | filter by subcategory (OR) |
| `tagIds` | UUID list (comma-separated) | MOV14 | filter by tag (any-of) |
| `type` | enum | MOV15 | `INCOME` / `OUTCOME` / `TRANSFER`, case-insensitive |
| `dateFrom` | date | MOV16 | inclusive lower bound |
| `dateTo` | date | MOV16 | inclusive upper bound |
| `search` | string | MOV17 | case-insensitive description match |

```
GET /api/movements?categoryIds=a,b&tagIds=x,y&dateFrom=2026-07-01&search=taxi
```

Semantics:

- Within one dimension: OR. Across dimensions: AND.
- Missing/empty dimension → ignored.
- `dateFrom` after `dateTo` → empty list, not an error.
- No params → all of the user's movements.

Responses: `200` (may be empty), `400` invalid filter value, `401`
unauthenticated.

## Data Model

No schema change. Filtering reads `movement` and `movement_tags`.

## Out of Scope

- Pagination and sorting; no order is guaranteed.
- Enriching results with category/subcategory/tag names.
- Body-based search or a query DSL.
- Month/year shortcut params (a UI concern).
