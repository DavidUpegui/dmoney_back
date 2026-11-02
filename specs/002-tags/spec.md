# Tags

Tags label movements across the category tree so the user can group spending
by theme (vacation, work, family) without depending on categories. A user owns
their tags; tags are never shared and never visible to anyone else.

## Requirements

- **TAG01** — A user can create a tag with a name and an optional description;
  the response is `201` with the new tag's `id`, `name` and `description`.
- **TAG02** — The name is required, at most 255 characters, and stored
  trimmed. A missing, blank or too long name is rejected with `400`.
- **TAG03** — The description is optional and stored trimmed. A missing
  description becomes empty; more than 255 characters is rejected.
- **TAG04** — A user cannot have two tags with the same name ignoring case;
  creating a duplicate returns `409`.
- **TAG05** — Every operation is limited to the authenticated user. A tag that
  belongs to another user is indistinguishable from one that does not exist:
  both return `404`, and listing never includes it.
- **TAG06** — A user can list their tags; when there are none, the response is
  `200` with an empty array.
- **TAG07** — A user can read one of their tags by id; a missing or foreign id
  returns `404`.
- **TAG08** — A tag can be updated partially: only the fields present in the
  request change, and the response is `200` with the updated tag.
- **TAG09** — Renaming a tag to a name already used by another tag of the same
  user returns `409`. Changing only the casing of its own name is allowed.
- **TAG10** — Updating a tag with a blank name returns `400`.
- **TAG11** — A user can delete one of their tags and the response is `204`;
  deleting a missing or foreign tag returns `404`.

## API Contract

| Method | Path | Req | Success | Errors |
|--------|------|-----|---------|--------|
| `POST` | `/api/tags` | TAG01, TAG02, TAG03, TAG04 | `201` | `400`, `401`, `409` |
| `GET` | `/api/tags` | TAG06 | `200` | `401` |
| `GET` | `/api/tags/{id}` | TAG07 | `200` | `401`, `404` |
| `PATCH` | `/api/tags/{id}` | TAG08, TAG09, TAG10 | `200` | `400`, `401`, `404`, `409` |
| `DELETE` | `/api/tags/{id}` | TAG11 | `204` | `401`, `404` |

TAG05 applies to every row. All errors use the `ApiError` body (`status`,
`error`, `message`, `timestamp`).

### Request / Response

```json
POST /api/tags
{ "name": "vacation", "description": "Trip to Italy" }
```

```json
201 Created
{ "id": "uuid", "name": "vacation", "description": "Trip to Italy" }
```

```json
PATCH /api/tags/{id}
{ "name": "trips" }
```

Omitted or `null` fields are left unchanged; an empty `description` clears it.

## Data Model

| Entity | Fields | Constraints |
|--------|--------|-------------|
| `tag` | `id`, `user_id`, `name`, `description` | `id` UUID PK; `user_id` not null; `name` varchar(255) not null; `description` varchar(500) |
| `movement_tags` | `movement_id`, `tag_id` | composite PK; FK to `movement` with `ON DELETE CASCADE` |

Value objects (immutable, self-validating):

- `TagId` — UUID, not null.
- `TagName` — trimmed, not blank.
- `TagDescription` — trimmed, `null` becomes empty, at most 255 characters.

## Out of Scope

- Ordering of the list; no order is guaranteed.
- A maximum length for the name (the database caps it at 255).
- Tag colours, merging, bulk operations.
- What happens to `movement_tags` rows when a tag is deleted is not defined by
  this capability (see `OBSERVATIONS.md`).
- Front-end behaviour.
