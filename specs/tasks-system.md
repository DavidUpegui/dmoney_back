# Tasks: System

Work blocks for `SYSTEM.md`. Each `SYS` requirement is verified by tests
wherever the behaviour is implemented (usually the security filter, the
global exception handler and the integration tests).

## SYS01 — 401 without a valid JWT
- [x] Added `sys01_*` tests for missing, invalid, malformed and expired tokens
- [x] Asserted the `ApiError` body on the 401
- [x] **Validado** — `sys01_*` (5 tests)

## SYS02 — No cross-user data
- [x] Added an end-to-end owner-scoping test with two users and two tokens
- [x] Covered read, update and delete of another user's tag (`404`) and an
      empty list for the other user
- [x] **Validado** — `sys02_*` (1 test)

## SYS03 — Invalid input never returns 500
- [x] ID value objects (`TagId`, `CategoryId`, `SubcategoryId`, `MovementId`)
      now throw `ValidationException` on malformed input
- [x] `SubcategoryDoesNotBelongToCategoryException` is a `ValidationException`,
      so it maps to `400`
- [x] Added `sys03_*` tests for malformed path variables, an invalid category
      type, an over-long tag name and a mismatched subcategory
- [x] **Validado** — `sys03_*` (6 tests)

## SYS04 — One error body shape
- [x] Added a `sys04_*` test asserting `status`, `error`, `message` and
      `timestamp` on `400`, `401` and `404`
- [x] Added a `sys04_*` test for `409`
- [x] **Validado** — `sys04_*` (2 tests)
