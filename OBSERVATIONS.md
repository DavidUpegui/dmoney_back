# Observations

A scratchpad for cross-cutting observations, not a task list. Add a dated
one-liner when you notice something that shouldn't be fixed right now; delete
the line once it's resolved. Feature-specific "not now" items belong in a
spec's "Out of Scope", not here.

## 2026-10-30

- Deleting a category or subcategory with movements is blocked with `409`. A future flow should let the user pick a destination category/subcategory for those movements instead of blocking (see `specs/004-categories/spec.md`).

## 2026-10-28

- Deleting a tag leaves its rows in `movement_tags`: the join table has no FK to `tag` and nothing cleans them up.
- Tag name uniqueness is enforced in the application only; there is no unique index on `(user_id, name)`, so concurrent creates can produce duplicates.
