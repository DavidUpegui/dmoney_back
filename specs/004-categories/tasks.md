# Tasks: Categories

## CAT01 — Create a category
- [x] `Category` aggregate + `CategoryId` (`domain`)
- [x] `CreateCategoryUseCase` (`application`)
- [x] `POST /api/categories` → `201` (`infrastructure`)
- [x] `category` table (`Liquibase` 001)
- [x] **Validado** — `cat01_*` (5 tests)

## CAT02 — Name required, trimmed, max 255
- [x] `CategoryName` (`domain`)
- [x] `@NotBlank` on the request (`infrastructure`)
- [x] **Validado** — `cat02_*` (8 tests)

## CAT03 — Optional description, max 255
- [x] `CategoryDescription` (`domain`)
- [x] **Validado** — `cat03_*` (7 tests)

## CAT04 — Type INCOME/OUTCOME
- [x] `CategoryType.from` rejects anything else (`domain`)
- [x] `400` mapping (`infrastructure`)
- [x] **Validado** — `cat04_*` (4 tests)

## CAT05 — Unique name per user
- [x] `CategoryUniquenessChecker` (`domain`)
- [x] Rename excludes the category itself (`application`)
- [x] **Validado** — `cat05_*` (12 tests)

## CAT06 — List own categories
- [x] `FindAllCategoriesUseCase` + `GET /api/categories` (`application` + `infrastructure`)
- [x] **Validado** — `cat06_*` (6 tests)

## CAT07 — Find one own category
- [x] `FindCategoryByIdUseCase` + `404` (`application` + `infrastructure`)
- [x] **Validado** — `cat07_*` (5 tests)

## CAT08 — Partial update
- [x] `EditCategoryUseCase` (`application`)
- [x] `PATCH /api/categories/{id}` → `200` (`infrastructure`)
- [x] **Validado** — `cat08_*` (11 tests)

## CAT09 — Delete own category
- [x] `DeleteCategoryUseCase` + `DELETE` → `204` (`application` + `infrastructure`)
- [x] **Validado** — `cat09_*` (6 tests)

## CAT10 — Delete cascades subcategories
- [x] Adapter loads the aggregate and deletes it, so JPA cascades to subcategories (`infrastructure`)
- [x] **Validado** — `cat10_*` (1 tests)

## CAT11 — Block delete when the category has movements
- [x] `MovementRepository.existsByUserIdAndCategoryId` (`domain` + `infrastructure`)
- [x] `ResourceInUseException` → `409` (`application` + `infrastructure`)
- [x] **Validado** — `cat11_*` (3 tests)

## CAT12 — Add a subcategory
- [x] `Category.addSubcategory` + `AddSubcategoryUseCase` (`domain` + `application`)
- [x] `POST .../subcategories` → `201` (`infrastructure`)
- [x] **Validado** — `cat12_*` (18 tests)

## CAT13 — Unique subcategory name within the category
- [x] Duplicate check on add and rename (`domain`)
- [x] `409` mapping (`infrastructure`)
- [x] **Validado** — `cat13_*` (5 tests)

## CAT14 — List subcategories
- [x] `FindAllSubcategoriesByCategoryIdUseCase` + `GET` (`application` + `infrastructure`)
- [x] **Validado** — `cat14_*` (5 tests)

## CAT15 — Partial update of a subcategory
- [x] `Category.editSubcategory` + `EditSubcategoryUseCase` (`domain` + `application`)
- [x] `PATCH .../subcategories/{id}` → `200` (`infrastructure`)
- [x] **Validado** — `cat15_*` (14 tests)

## CAT16 — Delete a subcategory
- [x] `Category.deleteSubcategory` + `DeleteSubcategoryUseCase` (`domain` + `application`)
- [x] `MovementRepository.existsByUserIdAndSubcategoryId` + `409` (`application` + `infrastructure`)
- [x] **Validado** — `cat16_*` (8 tests)

## CAT17 — Owner scoping
- [x] Every use case resolves the `UserId` from the token
- [x] Repository queries are scoped by `user_id`; foreign resources → `404`
- [x] **Validado** — `cat17_*` (7 tests)

## CAT18 — Optional category icon, stored as opaque text
- [x] `CategoryIcon` value object: trim, max 48, blank → `null` (`domain`)
- [x] `icon` on `Category` create/rehydrate/change (`domain`)
- [x] `icon` in `CreateCategoryCommand` and `EditCategoryCommand` (`application`)
- [x] `icon` on `CategoryEntity` + `CategoryMapper` (`infrastructure`)
- [x] Liquibase `006` adds `category.icon` (`Liquibase`)
- [x] **Validado** — `cat18_*` (9 tests)

## CAT19 — Optional category color, stored as opaque text
- [x] `CategoryColor` value object: trim, max 32, blank → `null` (`domain`)
- [x] `color` on `Category` create/rehydrate/change (`domain`)
- [x] `color` in `CreateCategoryCommand` and `EditCategoryCommand` (`application`)
- [x] `color` on `CategoryEntity` + `CategoryMapper` (`infrastructure`)
- [x] Liquibase `006` adds `category.color` (`Liquibase`)
- [x] **Validado** — `cat19_*` (7 tests)

## CAT20 — Category responses expose icon and color
- [x] `icon` and `color` in `CategoryResult` (`application`)
- [x] `@Size` limits on the category request DTOs, null passthrough (`infrastructure`)
- [x] Delete dead `CategoryWebMapper` / `CategoryResponse` (`infrastructure`)
- [x] Response JSON keeps `null` when nothing was chosen (`infrastructure`)
- [x] **Validado** — `cat20_*` (2 tests)

## CAT21 — PATCH clears or updates category icon and color
- [x] Absent field keeps the current value (`application`)
- [x] Empty string clears the field to `null` (`application` + `domain`)
- [x] Value over the maximum length → `400` (`infrastructure`)
- [x] `PATCH` → `200` with the updated category (`infrastructure`)
- [x] **Validado** — `cat21_*` (6 tests)

## CAT22 — Subcategory icon and color, nullable for inheritance
- [x] Nullable `icon`/`color` on `Subcategory` + change methods (`domain`)
- [x] `Category.addSubcategory` / `editSubcategory` accept both (`domain`)
- [x] Commands and `SubcategoryResult` carry both (`application`)
- [x] `SubcategoryEntity` + `CategoryMapper` round-trip the nulls (`infrastructure`)
- [x] Liquibase `006` adds `subcategory.icon` and `subcategory.color` (`Liquibase`)
- [x] **Validado** — `cat22_*` (10 tests)

## CAT23 — Subcategory responses expose nullable icon and color
- [x] `icon` and `color` in `SubcategoryResult`, never resolved (`application`)
- [x] `null` preserved in create, list and edit responses (`infrastructure`)
- [x] **Validado** — `cat23_*` (2 tests)
