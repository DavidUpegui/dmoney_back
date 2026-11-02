# Tasks: Tags

Implemented before the requirement model existed; this record is a retrofit.
The order below is the capability, not the original commit order.

## TAG01 — Create a tag
- [x] `Tag` aggregate + `TagId` (`domain`)
- [x] `CreateTagUseCase` (`application`)
- [x] `POST /api/tags` → `201` (`infrastructure`)
- [x] `tag` table (`Liquibase` 003)
- [x] **Validado** — `tag01_*` (4 tests)

## TAG02 — Required, trimmed name; blank or too long → 400
- [x] `TagName` trims, rejects null/blank and caps at 255 (`domain`)
- [x] `@NotBlank` on the creation request (`infrastructure`)
- [x] `ValidationException` → `400` (`infrastructure`)
- [x] **Validado** — `tag02_*` (9 tests)

## TAG03 — Optional description; trimmed; max 255
- [x] `TagDescription` trims, `null` → empty, rejects > 255 (`domain`)
- [x] `fromNullable` in creation (`application`)
- [x] **Validado** — `tag03_*` (9 tests)

## TAG04 — Unique name per user, ignoring case → 409
- [x] `TagUniquenessChecker` (`domain`)
- [x] `existsByUserIdAndNameIgnoreCase` (`infrastructure`)
- [x] `ResourceAlreadyExistsException` → `409` (`infrastructure`)
- [x] **Validado** — `tag04_*` (7 tests)

## TAG05 — Owner scoping
- [x] `AuthenticatedUserProvider` in every use case (`application`)
- [x] Repository queries scoped by `user_id` (`infrastructure`)
- [x] Missing or foreign resource → `404`
- [x] **Validado** — `tag05_*` (3 tests)

## TAG06 — List own tags
- [x] `FindAllTagsUseCase` (`application`)
- [x] `GET /api/tags` (`infrastructure`)
- [x] **Validado** — `tag06_*` (5 tests)

## TAG07 — Find one own tag by id
- [x] `FindTagByIdUseCase` (`application`)
- [x] `GET /api/tags/{id}` and `404` (`infrastructure`)
- [x] **Validado** — `tag07_*` (7 tests)

## TAG08 — Partial update
- [x] `changeName` / `changeDescription` (`domain`)
- [x] `EditTagUseCase` updates only the fields present (`application`)
- [x] `PATCH /api/tags/{id}` → `200` (`infrastructure`)
- [x] **Validado** — `tag08_*` (6 tests)

## TAG09 — Rename conflict rules
- [x] `TagUniquenessChecker.check(userId, newName, currentName)` (`domain`)
- [x] `409` on rename conflict (`infrastructure`)
- [x] **Validado** — `tag09_*` (6 tests)

## TAG10 — Blank name on update → 400
- [x] Non-blank `@Pattern` on the edition request (`infrastructure`)
- [x] `400` mapping (`infrastructure`)
- [x] **Validado** — `tag10_*` (1 test)

## TAG11 — Delete own tag
- [x] `DeleteTagUseCase` (`application`)
- [x] `DELETE /api/tags/{id}` → `204` (`infrastructure`)
- [x] `404` when nothing was deleted (`infrastructure`)
- [x] **Validado** — `tag11_*` (6 tests)
