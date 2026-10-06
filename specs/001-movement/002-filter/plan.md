# Plan: Movement — Filter & Search

## Domain layer (`tracker/domain/movement/...`)

- [ ] `MovementFilter` value object (parses/validates filter inputs)
- [ ] Change `MovementRepository.findAllByUserId` to accept `MovementFilter`

## Application layer (`tracker/application/movement/...`)

- [ ] `FindAllMovementsUseCase.execute(MovementFilter)` (was `execute()`)

## Infrastructure layer (`tracker/infrastructure/...`)

- [ ] `MovementJpaRepository` extends `JpaSpecificationExecutor`
- [ ] `MovementRepositoryAdapter` builds JPA `Specification` from `MovementFilter`
- [ ] `MovementController` findAll accepts optional `@RequestParam` filters

## Tests (`src/test/...`)

- [ ] `MovementFilter` value object tests
- [ ] `FindAllMovementsUseCase` tests (filter passthrough)
- [ ] `MovementRepositoryAdapter` filter tests (`@DataJpaTest`)
- [ ] `MovementController` findAll filter tests + malformed-type `400`
