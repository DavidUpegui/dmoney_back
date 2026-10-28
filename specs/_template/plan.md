# Plan: <Feature Name>

Break the spec into concrete work items, mapped to the hexagonal architecture
layers. Tick them off in `tasks.md` as they are implemented.

## Domain layer (`.../domain/...`)

- [ ] Aggregate / entity + value objects
- [ ] Repository interface
- [ ] Domain service / validator (if any)

## Application layer (`.../application/...`)

- [ ] Commands / responses
- [ ] Use cases

## Infrastructure layer (`.../infrastructure/...`)

- [ ] REST controller + DTOs + web mapper
- [ ] JPA entity + repository adapter + persistence mapper
- [ ] Liquibase changelog (if schema changes)

## Tests (`src/test/...`)

- [ ] Unit tests per value object
- [ ] Use case tests
- [ ] Controller / integration tests
- [ ] Coverage ≥ 80%
