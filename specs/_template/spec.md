# <Capability>

<Who uses it and what problem it solves. Two or three lines.>

## Requirements

- **XXX01** — <One verifiable guarantee, in business language.>
- **XXX02** — <Another one. If it needs a second rule, it needs a second ID.>

## API Contract

| Method | Path | Req | Success | Errors |
|--------|------|-----|---------|--------|
| `POST` | `/api/<resource>` | XXX01, XXX02 | `201` | `400`, `401`, `409` |

### Request / Response

```json
POST /api/<resource>
{ "field": "value" }
```

### Errors

All errors use the `ApiError` body (`status`, `error`, `message`,
`timestamp`).

## Data Model

| Entity | Fields | Constraints |
|--------|--------|-------------|
| `X`    | `id`, ... | `id` UUID PK, ... |

Value objects are immutable and self-validating. List each one with its
invariants.

## Out of Scope

- <Deliberately excluded. Keep it short.>
