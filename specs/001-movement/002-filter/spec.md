# Feature: Movement — Filter & Search

- **Status:** `implemented`
- **Folder:** `001-movement/002-filter`
- **Related module:** `tracker`

## 1. Overview

Extends `GET /api/movements` with optional filtering and text search. Filtering
supports **multi-select** for categories, subcategories, and tags. All
parameters are combinable with AND semantics; omitting all of them returns the
current user's full movement list. Depends on `001-crud`.

## 2. Requirements

### Functional (FR)

- **FR-1** — Filter movements by one or more categories (OR within the selection).
- **FR-2** — Filter movements by one or more subcategories (OR within the selection).
- **FR-3** — Filter movements by one or more tags (any-of, OR within the selection).
- **FR-4** — Filter movements by type.
- **FR-5** — Filter movements by inclusive date range (`dateFrom`/`dateTo`).
- **FR-6** — Search movements by case-insensitive description text.
- **FR-7** — Combine any dimensions with AND semantics.
- **FR-8** — No params → return all of the current user's movements.

### Non-functional (NFR)

- **NFR-1** — Filtering is always scoped to the current `UserId`.
- **NFR-2** — Invalid filter values (bad UUID, bad type) → `400`.
- **NFR-3** — Coverage ≥ 80%, one test per acceptance criterion.

## 3. API Contract

### `GET /api/movements`

| Param          | Type     | Meaning |
|----------------|----------|---------|
| `categoryIds`  | UUID list (comma-separated) | filter by category (OR) |
| `subcategoryIds`| UUID list (comma-separated) | filter by subcategory (OR) |
| `tagIds`       | UUID list (comma-separated) | filter by tag, any-of (OR) |
| `type`         | enum     | `INCOME` / `OUTCOME` / `TRANSFER` (case-insensitive) |
| `dateFrom`     | date     | inclusive lower bound |
| `dateTo`       | date     | inclusive upper bound |
| `search`       | string   | case-insensitive description match |

Example:

```
GET /api/movements?categoryIds=a,b&tagIds=x,y&dateFrom=2026-07-01&search=taxi
```

Semantics:

- Within a dimension (categories/subcategories/tags): **OR**.
- Across dimensions: **AND**.
- Selecting a category matches all its subcategories (no expansion needed).
- Category and subcategory filters are independent; the UI scopes the
  subcategory picker to the selected categories.

### Status codes

- `200` — success (list may be empty)
- `400` — invalid filter value (malformed UUID / type)
- `401` — unauthenticated

## 4. Data Model

No schema change. Filtering reads existing `movement` and `movement_tags`.

## 5. Business Rules & Validation

- Filters only ever return the current user's movements.
- `type` is parsed case-insensitively.
- `search` is trimmed and matched case-insensitively against `description`.
- `dateFrom`/`dateTo` are inclusive.
- Malformed `categoryIds`/`subcategoryIds`/`tagIds`/`type` → `400`.

## 6. Edge Cases

- Filter returns no matches → empty list `200`.
- `search` is blank/whitespace → ignored.
- `dateFrom` after `dateTo` → empty list (no error).
- Empty selection list → that dimension is ignored.

## 7. Acceptance Criteria

- **AC-1** — Given `categoryIds`, when list, then only movements in those categories.
- **AC-2** — Given `subcategoryIds`, when list, then only movements in those subcategories.
- **AC-3** — Given `tagIds`, when list, then only movements having any of those tags.
- **AC-4** — Given `type`, when list, then only movements of that type (case-insensitive).
- **AC-5** — Given `dateFrom`/`dateTo`, when list, then only movements in range (inclusive).
- **AC-6** — Given `search`, when list, then only movements whose description contains the text (case-insensitive).
- **AC-7** — Given multiple dimensions, when list, then only movements matching all (AND).
- **AC-8** — Given no params, when list, then all of the current user's movements.
- **AC-9** — Given a filter matching another user's movement, when list, then it is not returned.
- **AC-10** — Given a malformed `type` or UUID, when list, then `400`.
- **AC-11** — Given no token, when list, then `401`.

## 8. Out of Scope

- Pagination/sorting.
- Enriching results with category/subcategory/tag names.
- POST/body-based search (only if filters become a query DSL later).
- Month/year shortcut params (a UI concern).

## Changelog

- `2026-10-05` — Initial spec; single-value filters.
- `2026-10-05` — **Breaking change**: `categoryId`/`subcategoryId`/`tagId`
  replaced by multi-value `categoryIds`/`subcategoryIds`/`tagIds`
  (comma-separated). Tag semantics = any-of.
