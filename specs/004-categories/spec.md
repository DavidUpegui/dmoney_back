# Categories

A category groups movements by type (`INCOME`/`OUTCOME`) and owns a set of
subcategories. Everything is scoped to the authenticated user.

## Requirements

- **CAT01** — A user can create a category with a name, an optional description
  and a type; the response is `201` with the created category.
- **CAT02** — The name is required, trimmed and at most 255 characters; invalid
  input returns `400`.
- **CAT03** — The description is optional, trimmed and at most 255 characters.
- **CAT04** — The type is `INCOME` or `OUTCOME`; any other value returns `400`.
- **CAT05** — A user cannot have two categories with the same name ignoring
  case; creating or renaming to a duplicate returns `409`.
- **CAT06** — A user can list their own categories.
- **CAT07** — A user can read one of their categories by id; a missing or
  foreign id returns `404`.
- **CAT08** — A category can be updated partially; only the fields present
  change and the response is `200` with the updated category.
- **CAT09** — A user can delete one of their categories; the response is `204`,
  and a missing or foreign id returns `404`.
- **CAT10** — Deleting a category also deletes its subcategories.
- **CAT11** — Deleting a category that still has movements is rejected with
  `409`; the category and its movements stay untouched.
- **CAT12** — A user can add a subcategory to one of their categories; the
  name and description follow the same rules as a category.
- **CAT13** — A subcategory name is unique within its category, ignoring case;
  a duplicate returns `409` on create or rename.
- **CAT14** — A user can list the subcategories of one of their categories; a
  missing or foreign category returns `404`.
- **CAT15** — A subcategory can be updated partially; only the fields present
  change and the response is `200`.
- **CAT16** — A user can delete a subcategory; the response is `204`, a missing
  or foreign id returns `404`, and a subcategory that still has movements is
  rejected with `409`.
- **CAT17** — Every category and subcategory operation is scoped to the
  authenticated user; another user's resource behaves as if it did not exist.
- **CAT18** — A category has an optional `icon`: a free-form text identifier
  supplied by the client (for example, an icon-library name). It is trimmed and
  at most 48 characters; absent or blank is stored as `null`. The back-end does
  not validate it against any catalog.
- **CAT19** — A category has an optional `color`: a free-form text token
  supplied by the client. It is trimmed and at most 32 characters; absent or
  blank is stored as `null`. The back-end does not validate it against any
  palette and supplies no default.
- **CAT20** — Every category response includes `icon` and `color`, returned
  exactly as stored; either may be `null` and is never substituted.
- **CAT21** — `PATCH /api/categories/{id}` accepts `icon` and `color`: an
  absent field keeps its current value, an empty string clears it to `null`, a
  value over the maximum length returns `400`, and a valid change returns `200`
  with the updated category.
- **CAT22** — A subcategory has an optional `icon` and an optional `color`
  under the same rules as CAT18, CAT19 and CAT21. `null` means the subcategory
  inherits its category's value; the back-end never resolves, copies or
  defaults it.
- **CAT23** — Every subcategory response includes `icon` and `color`; either
  may be `null` and is never substituted by the back-end.

## API Contract

| Method | Path | Req | Success | Errors |
|--------|------|-----|---------|--------|
| `POST` | `/api/categories` | CAT01, CAT02, CAT03, CAT04, CAT05, CAT18, CAT19 | `201` | `400`, `401`, `409` |
| `GET` | `/api/categories` | CAT06 | `200` | `401` |
| `GET` | `/api/categories/{id}` | CAT07 | `200` | `401`, `404` |
| `PATCH` | `/api/categories/{id}` | CAT05, CAT08, CAT21 | `200` | `400`, `401`, `404`, `409` |
| `DELETE` | `/api/categories/{id}` | CAT09, CAT10, CAT11 | `204` | `401`, `404`, `409` |
| `POST` | `/api/categories/{categoryId}/subcategories` | CAT12, CAT13, CAT22 | `201` | `400`, `401`, `404`, `409` |
| `GET` | `/api/categories/{categoryId}/subcategories` | CAT14 | `200` | `401`, `404` |
| `PATCH` | `/api/categories/{categoryId}/subcategories/{subcategoryId}` | CAT13, CAT15, CAT22 | `200` | `400`, `401`, `404`, `409` |
| `DELETE` | `/api/categories/{categoryId}/subcategories/{subcategoryId}` | CAT16 | `204` | `401`, `404`, `409` |

CAT17 applies to every row. All errors use the `ApiError` body. CAT20 applies
to every category response and CAT23 to every subcategory response.

## Data Model

| Entity | Fields | Constraints |
|--------|--------|-------------|
| `category` | `id`, `user_id`, `name`, `description`, `type`, `icon`, `color` | `id` UUID PK; `user_id` not null; `name` varchar(255) not null; `type` varchar(20) not null; `icon` varchar(48) null; `color` varchar(32) null |
| `subcategory` | `id`, `name`, `description`, `category_id`, `icon`, `color` | `id` UUID PK; FK to `category`; `name` varchar(255) not null; `icon` varchar(48) null; `color` varchar(32) null |

Value objects:

- `CategoryName` / `SubcategoryName` — trimmed, not blank, max 255.
- `CategoryDescription` / `SubcategoryDescription` — trimmed, max 255.
- `CategoryType` — `INCOME` or `OUTCOME`, uppercase.
- `CategoryIcon` — optional opaque text, trimmed, max 48; blank becomes `null`.
- `CategoryColor` — optional opaque text, trimmed, max 32; blank becomes
  `null`.

## Out of Scope

- Validating `icon` against an icon-provider catalog, or `color` against a
  palette; both are opaque to the back-end.
- Supplying icon/color defaults or resolving subcategory inheritance
  server-side.
- Storing icon image assets or uploads.
- Ordering of the category and subcategory lists; no order is guaranteed.
- Reassigning movements when a category or subcategory is deleted; deletion is
  blocked instead (see `OBSERVATIONS.md`).
- More than two levels of hierarchy.
- Pagination.
