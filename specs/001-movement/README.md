# Feature: Movement

A movement records a single financial transaction for a user. This feature is
split into vertical slices:

| Slice | Status | Description | Depends on |
|-------|--------|-------------|------------|
| `001-crud` | implemented | Create, list, find-by-id, update, delete | — |
| `002-filter` | draft | Filter & search the movement list | `001-crud` |

Build order: `001-crud` first, then `002-filter`.
