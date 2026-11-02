# System

Requirements that hold for the whole API, independent of any capability.
They are written once here and never repeated in a capability spec.

## Requirements

- **SYS01** — A request without a valid JWT is rejected with `401` and an
  `ApiError` body.
- **SYS02** — No endpoint returns another user's data. A resource owned by
  another user is indistinguishable from a missing one (`404`).
- **SYS03** — Invalid client input never produces `500`. Malformed payloads,
  malformed identifiers and failed validation map to `400` or `404` with an
  `ApiError` body.
- **SYS04** — Every error response uses the same body shape: `status`,
  `error`, `message`, `timestamp`.

## Out of Scope

- Build rules (coverage, Liquibase append-only, value object invariants) are
  conventions, not requirements. They live in `AGENTS.md` and `docs/`.
