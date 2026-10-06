# Observations

A scratchpad for cross-cutting observations, not a task list. Add a dated
one-liner when you notice something that shouldn't be fixed right now; delete
the line once it's resolved. Feature-specific "not now" items belong in a
spec's "Out of Scope", not here.

## 2026-10-05

- `CategoryController` and `TagController` return `201 Created` from their `@PatchMapping` edit endpoints; REST semantics should return `200 OK`. The new movement PATCH returns `200`. Revisit category/tag once their specs are (re)written.
