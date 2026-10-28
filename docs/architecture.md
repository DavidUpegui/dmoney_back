# Architecture

`dmoney` is a hexagonal Spring Boot application. Dependencies point inward:
infrastructure depends on application and domain; domain depends on nothing
but the `shared` kernel.

## Bounded contexts

| Context    | Responsibility                                              |
|------------|-------------------------------------------------------------|
| `auth`     | `User` aggregate, registration, login, JWT + BCrypt          |
| `tracker`  | `Category` (→ `Subcategory`), `Tag`, `Movement` aggregates    |
| `shared`   | `UserId`, domain exceptions, global exception handler, `ApiError` |

## Layer rules

- **domain** — aggregates are plain POJOs with static factories
  (`create`, `rehydrate`), immutable value objects that throw
  `ValidationException`, and repository *interfaces* (ports). No Spring,
  no JPA annotations.
- **application** — use cases orchestrate domain aggregates through ports.
  Commands and responses are Java records. Cross-cutting ports like
  `AuthenticatedUserProvider` live here.
- **infrastructure** — Spring `@RestController` + request/response DTOs and
  web mappers; JPA entities + persistence mappers + `*RepositoryAdapter`
  implementations of the domain ports; Liquibase changelogs.

## Domain model (tracker)

- `Category` owns a `Set<Subcategory>` (one-to-many, embedded in the aggregate).
- `Movement` references a `Category`, a `Subcategory`, and a `Set<TagId>`
  (join table `movement_tags`).
- All tracker aggregates carry a `UserId` for owner-scoping.

## DDD & hexagonal principles

These are the decision rules behind the layer layout above.

### Where do invariants live?

| Invariant concerns                          | Lives in            | Example in this repo                     |
|---------------------------------------------|---------------------|------------------------------------------|
| Validity of a single field                  | Value object        | `Amount` rejects `<= 0`                  |
| Cross-field / collection rule within a root | Aggregate method    | duplicate subcategory name in `Category`  |
| Rule spanning multiple aggregates           | Domain service      | `MovementCategorizationValidator`         |
| Persistence-side uniqueness                 | Repository port / DB | `existsByUserIdAndNameIgnoreCase`         |

### Aggregate = transaction boundary

An aggregate is the unit of consistency: a use case mutates and saves **one**
aggregate per transaction. Never save multiple roots in a single use case, and
never mutate an aggregate's children except through the root's methods
(e.g. add/edit/delete subcategories go through `Category`).

### Reference other aggregates by identity, not by object

`Movement` holds `CategoryId`, `SubcategoryId`, and `TagId` — not the objects
themselves. Cross-aggregate references are by ID only; navigation happens
through the repository, never through JPA `@OneToMany`/`@ManyToOne` across roots.

### Factories: `create` vs `rehydrate`

- `create(...)` — builds a new aggregate, generating a fresh ID and enforcing
  all invariants.
- `rehydrate(...)` — reconstructs an already-persisted aggregate from storage,
  so the repository can return a fully-formed root without re-running
  creation-side rules.

Value objects are immutable and self-validating; they throw
`ValidationException` on invalid input so invalid state can never be
constructed.

## Security

- JWT is issued at `POST /auth/login`, validated by `JwtAuthenticationFilter`
  (stateless), and the authenticated `UserId` is surfaced via
  `AuthenticatedUserProvider` / `SpringSecurityAuthProvider`.
- `SecurityConfig` permits only `/auth/**` and OpenAPI endpoints; everything
  else requires a valid token.

## Persistence

- Liquibase manages the schema (`db/changelog/`). `ddl-auto: validate`.
- New schema changes always add a new changeset; never edit an applied one.
