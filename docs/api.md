# dmoney API

REST API for `dmoney`, a personal money tracker. This is the back-end's HTTP
reference for any client (web, mobile, or another service). It is maintained by
hand and updated in the same change set as the endpoint it describes. It
reflects current behavior, including a short list of client-visible deviations
at the end.

- **Runtime OpenAPI/Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`
- **Backend stack:** Spring Boot 3.3.5, PostgreSQL.

---

## 1. Conventions

| Topic | Convention |
|-------|------------|
| Base URL | `http://localhost:8080` (default Spring port; no context path) |
| Media type | `application/json` for all request and response bodies |
| IDs | UUID v4 as canonical lowercase string, e.g. `20000000-0000-0000-0000-000000000000` |
| Dates | ISO-8601 calendar date, `YYYY-MM-DD` (no time, no timezone) |
| Amounts | Decimal number as string/number, e.g. `85.50`; must be `> 0` |
| Timestamps (errors) | ISO-8601 instant, e.g. `2026-10-12T14:03:00.123Z` |
| Auth | `Authorization: Bearer <jwt>` |
| CORS | Allowed origins: `http://localhost:5173`, `http://localhost:8081`; methods `GET, POST, PATCH, DELETE`; credentials allowed |

### Authentication

- `POST /auth/register` and `POST /auth/login` are the only public endpoints.
- Every other endpoint requires a valid JWT bearer token.
- The JWT is an HS256 token. `sub` = the user's UUID. Expiry is **24 hours**.
- Missing/invalid/expired token on a protected endpoint → `401`.
- All resources are **owner-scoped**: a token only ever sees/affects that
  user's data. A resource owned by another user is treated as `404` (never
  `403`).

### Enums

| Enum | Values | Notes |
|------|--------|-------|
| `MovementType` | `INCOME`, `OUTCOME`, `TRANSFER` | Accepted **case-insensitively** on input; responses are uppercase |
| `CategoryType` | `INCOME`, `OUTCOME` | Must be **uppercase** (see Known deviations) |

---

## 2. Error format

All errors returned by `GlobalExceptionHandler` share this shape:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Category name is required.",
  "timestamp": "2026-10-12T14:03:00.123Z"
}
```

| `status` | When |
|----------|------|
| `400` | Bean-validation failure, malformed JSON, domain `ValidationException`, invalid filter UUID/type |
| `401` | Missing/invalid token, bad login credentials |
| `404` | Resource not found or owned by another user |
| `409` | Duplicate resource (category name, tag name, email) |
| `500` | Unexpected error |

Field-validation errors report only the **first** violation in `message`.

---

## 3. Auth

### `POST /auth/register`

Creates a local user.

Request:

```json
{
  "email": "ada@example.com",
  "password": "secret123",
  "name": "Ada Lovelace"
}
```

| Field | Rules |
|-------|-------|
| `email` | required, non-blank, must match `^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$`; normalized to lowercase; max 255 chars |
| `password` | required, non-blank, at least 8 characters |
| `name` | required, non-blank; trimmed; max 100 chars |

Responses:

- `201 Created`
  ```json
  { "userId": "10000000-0000-0000-0000-000000000000" }
  ```
- `400` validation error
- `409` email already registered

### `POST /auth/login`

Request:

```json
{ "email": "ada@example.com", "password": "secret123" }
```

Responses:

- `200 OK`
  ```json
  { "token": "eyJhbGciOiJIUzI1NiJ9..." }
  ```
- `400` validation error (missing fields)
- `401` invalid credentials

---

## 4. Categories and subcategories

A category belongs to the user, has a **type** (`INCOME`/`OUTCOME`) and zero or
more **subcategories**. Category names are unique per user, case-insensitive.

### Category object

```json
{
  "id": "20000000-0000-0000-0000-000000000000",
  "name": "Housing",
  "description": "Monthly housing costs",
  "type": "OUTCOME",
  "icon": "house",
  "color": "blue"
}
```

`icon` and `color` are opaque to the back-end: it stores the text the client
sends and returns it unchanged. It does not validate either against a catalog
or a palette and supplies no default, so either may be `null`.

### Subcategory object

```json
{
  "categoryId": "20000000-0000-0000-0000-000000000000",
  "subcategoryId": "30000000-0000-0000-0000-000000000000",
  "subcategoryName": "Rent",
  "subcategoryDescription": "Apartment rent",
  "icon": null,
  "color": null
}
```

A `null` `icon` or `color` on a subcategory means "inherit from its category".
The back-end never resolves or copies the inherited value.

### Endpoints

| Method | Path | Body | Success | Errors |
|--------|------|------|---------|--------|
| `POST` | `/api/categories` | `CreateCategoryRequest` | `201` Category | `400`, `401`, `409` |
| `GET` | `/api/categories` | — | `200` Category[] | `401` |
| `GET` | `/api/categories/{id}` | — | `200` Category | `401`, `404` |
| `PATCH` | `/api/categories/{id}` | `EditCategoryRequest` | `200` Category | `400`, `401`, `404`, `409` |
| `DELETE` | `/api/categories/{id}` | — | `204` | `401`, `404`, `409` |
| `POST` | `/api/categories/{categoryId}/subcategories` | `CreateSubcategoryRequest` | `201` Subcategory | `400`, `401`, `404`, `409` |
| `GET` | `/api/categories/{categoryId}/subcategories` | — | `200` Subcategory[] | `401`, `404` |
| `PATCH` | `/api/categories/{categoryId}/subcategories/{subcategoryId}` | `EditSubcategoryRequest` | `200` Subcategory | `400`, `401`, `404`, `409` |
| `DELETE` | `/api/categories/{categoryId}/subcategories/{subcategoryId}` | — | `204` | `401`, `404`, `409` |

### Request bodies

`CreateCategoryRequest`

```json
{
  "name": "Housing",
  "description": "Monthly housing costs",
  "type": "OUTCOME",
  "icon": "house",
  "color": "blue"
}
```

- `name`: required, non-blank, max 255 chars.
- `description`: optional; trimmed; max 255 chars (blank/null → `""`).
- `type`: required, non-blank; `INCOME` or `OUTCOME` (**uppercase**).
- `icon`: optional; trimmed; max 48 chars; blank/null → `null`.
- `color`: optional; trimmed; max 32 chars; blank/null → `null`.

`EditCategoryRequest` (PATCH — only non-null fields change)

```json
{
  "name": "Home",
  "description": "Rent + utilities",
  "type": "OUTCOME",
  "icon": "house",
  "color": "teal"
}
```

- `name`: optional; if present must not be blank and max 255 chars;
  uniqueness re-checked.
- `description`: optional; max 255 chars.
- `type`: optional; uppercase enum.
- `icon`: optional; absent keeps the value, `""` clears it to `null`, max 48.
- `color`: optional; absent keeps the value, `""` clears it to `null`, max 32.

`CreateSubcategoryRequest`

```json
{ "name": "Rent", "description": "Apartment rent", "icon": "key", "color": null }
```

- `name`: required, non-blank, max 255 chars.
- `description`: optional; max 255 chars.
- `icon`/`color`: optional, same limits as a category; `null` means inherit
  from the category.

`EditSubcategoryRequest` (PATCH): all fields optional; `name` if present must
not be blank and max 255 chars. An absent `icon`/`color` keeps the value and an
empty string clears it to `null` (inherit).

---

## 5. Tags

A tag belongs to the user. Tag names are unique per user, case-insensitive.

### Tag object

```json
{
  "id": "40000000-0000-0000-0000-000000000000",
  "name": "recurring",
  "description": "Monthly recurring expense"
}
```

### Endpoints

| Method | Path | Body | Success | Errors |
|--------|------|------|---------|--------|
| `POST` | `/api/tags` | `CreateTagRequest` | `201` Tag | `400`, `401`, `409` |
| `GET` | `/api/tags` | — | `200` Tag[] | `401` |
| `GET` | `/api/tags/{id}` | — | `200` Tag | `401`, `404` |
| `PATCH` | `/api/tags/{id}` | `EditTagRequest` | `200` Tag | `400`, `401`, `404`, `409` |
| `DELETE` | `/api/tags/{id}` | — | `204` | `401`, `404` |

### Request bodies

`CreateTagRequest`

```json
{ "name": "recurring", "description": "Monthly recurring expense" }
```

- `name`: required, non-blank, max 255 chars.
- `description`: optional; max 255 chars.

`EditTagRequest` (PATCH — only non-null fields change)

```json
{ "name": "subscriptions", "description": "Streaming + gym" }
```

- `name`: optional; if present must not be blank and max 255 chars;
  uniqueness re-checked.
- `description`: optional; max 255 chars.

---

## 6. Movements

A movement is a financial transaction owned by the user.

### Movement object

```json
{
  "movementId": "50000000-0000-0000-0000-000000000000",
  "categoryId": "20000000-0000-0000-0000-000000000000",
  "subcategoryId": "30000000-0000-0000-0000-000000000000",
  "amount": 85.50,
  "type": "OUTCOME",
  "description": "Rent",
  "date": "2026-07-14",
  "tagIds": ["40000000-0000-0000-0000-000000000000"]
}
```

### `POST /api/movements`

Request:

```json
{
  "categoryId": "20000000-0000-0000-0000-000000000000",
  "subcategoryId": "30000000-0000-0000-0000-000000000000",
  "amount": 85.50,
  "type": "OUTCOME",
  "description": "Rent",
  "date": "2026-07-14",
  "tagIds": ["40000000-0000-0000-0000-000000000000"]
}
```

| Field | Rules |
|-------|-------|
| `categoryId` | required UUID; must belong to the user (else `404`) |
| `subcategoryId` | required UUID; must belong to `categoryId` (else validation error) |
| `amount` | required; `> 0` |
| `type` | required; `INCOME`/`OUTCOME`/`TRANSFER`, case-insensitive |
| `description` | optional; max 500 chars; null → `""` |
| `date` | required; `YYYY-MM-DD` |
| `tagIds` | optional set of UUIDs; each must belong to the user (else `404`) |

Responses:

- `201 Created` with `Location: /api/movements/{movementId}` and the Movement body
- `400` validation error
- `404` category/tag not found or owned by another user
- `401` unauthenticated

### `GET /api/movements`

Lists the current user's movements with optional filtering. All parameters are
optional and combine with **AND** semantics.

| Param | Type | Meaning |
|-------|------|---------|
| `categoryIds` | UUID list | filter by category (OR within the list) |
| `subcategoryIds` | UUID list | filter by subcategory (OR) |
| `tagIds` | UUID list | movements having **any** of these tags (OR) |
| `type` | enum | `INCOME`/`OUTCOME`/`TRANSFER`, case-insensitive |
| `dateFrom` | date | inclusive lower bound |
| `dateTo` | date | inclusive upper bound |
| `search` | string | case-insensitive substring match on `description`; trimmed; blank ignored |

List params accept comma-separated values and/or repeated params, e.g.:

```
GET /api/movements?categoryIds=<a>,<b>&tagIds=<x>&dateFrom=2026-07-01&search=taxi
```

Semantics:

- Within one dimension: OR. Across dimensions: AND.
- Missing/empty dimension → ignored.
- Selecting a category matches movements in that category regardless of
  subcategory (no expansion needed).
- `dateFrom` after `dateTo` → empty list (not an error).
- No params → all of the user's movements.

Responses:

- `200 OK` Movement[] (may be empty)
- `400` malformed UUID in a list param or invalid `type`
- `401` unauthenticated

### `GET /api/movements/{id}`

- `200 OK` Movement
- `404` not found or owned by another user
- `401` unauthenticated

### `PATCH /api/movements/{id}`

Partial update. Only provided (non-null) fields change; omitted fields keep
their current value.

```json
{
  "categoryId": "20000000-0000-0000-0000-000000000000",
  "subcategoryId": "30000000-0000-0000-0000-000000000000",
  "amount": 90.00,
  "type": "OUTCOME",
  "description": "Rent (updated)",
  "date": "2026-07-15",
  "tagIds": []
}
```

- Passing `tagIds: []` clears all tags.
- When category/subcategory/tags change, ownership and
  subcategory-belongs-to-category are re-validated.
- Responses: `200` Movement; `400` validation; `404` movement/category/tag not
  found; `401` unauthenticated.

### `DELETE /api/movements/{id}`

- `204 No Content`
- `404` not found or owned by another user
- `401` unauthenticated

---

## 7. Endpoint summary

```
POST   /auth/register
POST   /auth/login

GET    /api/categories
POST   /api/categories
GET    /api/categories/{id}
PATCH  /api/categories/{id}
DELETE /api/categories/{id}
GET    /api/categories/{categoryId}/subcategories
POST   /api/categories/{categoryId}/subcategories
PATCH  /api/categories/{categoryId}/subcategories/{subcategoryId}
DELETE /api/categories/{categoryId}/subcategories/{subcategoryId}

GET    /api/tags
POST   /api/tags
GET    /api/tags/{id}
PATCH  /api/tags/{id}
DELETE /api/tags/{id}

GET    /api/movements
POST   /api/movements
GET    /api/movements/{id}
PATCH  /api/movements/{id}
DELETE /api/movements/{id}
```

---

## 8. Business rules

- Every query/command is scoped to the authenticated user.
- Category names and tag names are unique per user, case-insensitive.
- `CategoryType` must be uppercase (`INCOME`/`OUTCOME`); invalid values return `400`.
- A movement's category must belong to the user.
- A movement's subcategory must belong to the movement's category.
- A movement's tags must all belong to the user.
- Deleting a category deletes its subcategories. It is rejected with `409` if
  the category still has movements.
- Deleting a subcategory is rejected with `409` if it still has movements.
- `amount` must be strictly greater than zero.
- Descriptions are optional; unspecified/null descriptions are stored as `""`.

---

## 9. Known deviations (as of 2026-10-30)

These are current implementation quirks the client should be aware of. They are
known and may be fixed later.

1. **Tag controller base path** is declared as `api/tags` (no leading slash);
   it resolves to `/api/tags`.
