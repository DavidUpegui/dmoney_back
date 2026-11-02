# Tasks: Movement — Filter & Search

## MOV12 — Filter by category
- [x] `categoryIds` parsed and applied as OR (`application` + `infrastructure`)
- [x] **Validado** — `mov12_*` (4 tests)

## MOV13 — Filter by subcategory
- [x] `subcategoryIds` parsed and applied as OR (`application` + `infrastructure`)
- [x] **Validado** — `mov13_*` (1 tests)

## MOV14 — Filter by tags
- [x] `tagIds` applied as any-of (`infrastructure`)
- [x] **Validado** — `mov14_*` (2 tests)

## MOV15 — Filter by type
- [x] Case-insensitive type filter (`domain`)
- [x] **Validado** — `mov15_*` (1 tests)

## MOV16 — Date range
- [x] Inclusive `dateFrom` / `dateTo` (`infrastructure`)
- [x] **Validado** — `mov16_*` (1 tests)

## MOV17 — Search
- [x] Case-insensitive description match; blank ignored (`domain` + `infrastructure`)
- [x] **Validado** — `mov17_*` (2 tests)

## MOV18 — Combine dimensions and default
- [x] Dimensions combine with AND; no params returns everything (`infrastructure`)
- [x] **Validado** — `mov18_*` (4 tests)

## MOV19 — Invalid filter values
- [x] Malformed UUID / type → `ValidationException` → `400` (`domain`)
- [x] **Validado** — `mov19_*` (5 tests)

## MOV20 — Owner scoping in filters
- [x] Every filter query is scoped by `UserId` (`infrastructure`)
- [x] **Validado** — `mov20_*` (1 tests)
