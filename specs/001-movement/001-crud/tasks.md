# Tasks: Movement — CRUD

## MOV01 — Create a movement
- [x] `Movement` aggregate + `MovementId` (`domain`)
- [x] `CreateMovementUseCase` (`application`)
- [x] `POST /api/movements` → `201` + `Location` (`infrastructure`)
- [x] `movement` + `movement_tags` tables (`Liquibase` 004, 005)
- [x] **Validado** — `mov01_*` (5 tests)

## MOV02 — Amount > 0
- [x] `Amount` rejects zero and negative values (`domain`)
- [x] **Validado** — `mov02_*` (6 tests)

## MOV03 — Type case-insensitive
- [x] `MovementType.from` uppercases before parsing (`domain`)
- [x] **Validado** — `mov03_*` (6 tests)

## MOV04 — Optional description
- [x] `MovementDescription` trims, caps at 500 and maps `null` to empty (`domain`)
- [x] **Validado** — `mov04_*` (7 tests)

## MOV05 — Category ownership
- [x] `MovementCategorizationValidator` checks the category belongs to the user (`domain`)
- [x] **Validado** — `mov05_*` (2 tests)

## MOV06 — Subcategory belongs to the category
- [x] Validator rejects a mismatched subcategory → `400` (`domain`)
- [x] **Validado** — `mov06_*` (1 tests)

## MOV07 — Tag ownership
- [x] Validator checks every tag belongs to the user (`domain`)
- [x] **Validado** — `mov07_*` (2 tests)

## MOV08 — List own movements
- [x] `FindAllMovementsUseCase` + `GET /api/movements` (`application` + `infrastructure`)
- [x] **Validado** — `mov08_*` (5 tests)

## MOV09 — Find one own movement
- [x] `FindMovementByIdUseCase` + `404` (`application` + `infrastructure`)
- [x] **Validado** — `mov09_*` (6 tests)

## MOV10 — Partial update
- [x] Aggregate mutators + `EditMovementUseCase` (`domain` + `application`)
- [x] `PATCH /api/movements/{id}` → `200` (`infrastructure`)
- [x] `tagIds: []` clears tags and ownership is re-validated (`application`)
- [x] **Validado** — `mov10_*` (13 tests)

## MOV11 — Delete
- [x] `DeleteMovementUseCase` + `DELETE` → `204` (`application` + `infrastructure`)
- [x] **Validado** — `mov11_*` (6 tests)
