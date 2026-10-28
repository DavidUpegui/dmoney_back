# Plan: Movement — CRUD

## Domain layer (`tracker/domain/movement/...`)

- [x] `Movement` aggregate + value objects (`MovementId`, `Amount`, `MovementType`, `MovementDescription`)
- [x] `MovementRepository` (`save`, `findAllByUserId`)
- [x] `MovementCategorizationValidator`
- [ ] Add `findByUserIdAndId`, `deleteByUserIdAndId` to `MovementRepository`
- [ ] Add `Movement` mutators (`changeCategory`, `changeSubcategory`, `changeAmount`, `changeType`, `changeDescription`, `changeDate`, `changeTags`)

## Application layer (`tracker/application/movement/...`)

- [x] `CreateMovementUseCase`, `FindAllMovementsUseCase`, `CreateMovementCommand`, `MovementResponse`
- [ ] `FindMovementByIdUseCase`
- [ ] `EditMovementUseCase` + `EditMovementCommand`
- [ ] `DeleteMovementUseCase`
- [ ] Fix: use `MovementType.from` (case-insensitive) in `CreateMovementUseCase`
- [ ] Fix: use `MovementDescription.fromNullable` for nullable `description`

## Infrastructure layer (`tracker/infrastructure/...`)

- [x] `MovementController` (create/list), `CreateMovementRequest`, entity, adapter, mapper
- [ ] Add `GET`/`PATCH`/`DELETE` by-id endpoints + `EditMovementRequest`
- [ ] No schema change expected (no new Liquibase changeset)

## Tests (`src/test/...`)

- [ ] Value object tests (`Amount`, `MovementType`, `MovementDescription`, `MovementId`)
- [ ] `Movement` mutator tests
- [ ] `MovementCategorizationValidator` tests
- [ ] Use case tests (create / find-all / find-by-id / edit / delete)
- [ ] Controller tests (`@WebMvcTest`) + security integration test
- [ ] Re-enable the `test` task in `build.gradle`
