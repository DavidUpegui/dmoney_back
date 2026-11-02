# Movement

A movement records a single financial transaction for a user. The capability is
split into two vertical slices:

| Slice | Spec | Description | Depends on |
|-------|------|-------------|------------|
| `001-crud` | modeled | Create, list, find, update, delete | — |
| `002-filter` | modeled | Filter and search the movement list | `001-crud` |

Build order: `001-crud` first, then `002-filter`.
