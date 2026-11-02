# Testing conventions

## Layout

Tests mirror the main source tree, package-by-layer, under `src/test/java`:

```
src/test/java/com/dmoney/dmoney/
  auth/
    domain/...        # value object + aggregate tests
    application/...   # use case tests (mock ports)
    infrastructure/... # controller (@WebMvcTest), security, adapter tests
  tracker/
    domain/...
    application/...
    infrastructure/...
  shared/...
```

## Test categories

- **Value objects / aggregates** — plain unit tests, no Spring context. Verify
  self-validation (invalid input throws `ValidationException`) and mutators.
- **Use cases** — unit tests with the domain repository ports mocked
  (Mockito). Verify orchestration, owner-scoping, and error mapping.
- **Controllers** — `@WebMvcTest` with the use case mocked. Verify HTTP status
  codes, request validation (`@Valid`), and response body shape.
- **Persistence adapters** — `@DataJpaTest` against H2. Verify mapping
  (domain ↔ entity) and query correctness. Tests use `ddl-auto: create-drop`
  for speed; `LiquibaseMigrationTest` applies the real changelog to an empty
  database with `ddl-auto: validate`, so schema drift is caught.
- **Security** — `SecurityIntegrationTest` asserts `401` for missing, invalid
  and malformed tokens, and `200` with a valid one. Cross-user isolation is
  asserted at the adapter/use-case level (see `tag05_*`); there is no
  end-to-end cross-user test yet.

## Requirement traceability

Tests that verify a requirement start their name with the requirement ID in
lowercase:

```java
void tag02_rejectsDuplicateNameForSameUser()
void sys01_returns401WhenNoJwt()
```

- One requirement may have several tests, in different layers; all of them
  carry the ID.
- The name is the link. `grep -ri tag02` finds the requirement, its tasks and
  its tests.
- Tests of pure technical helpers (formatting, mappers with no requirement)
  do not need an ID.
- The audit reads the name Gradle reports. Do not add a `@DisplayName` to a
  requirement test unless it also starts with the ID.

```bash
./gradlew requirementsCheck
```

Cross-checks the IDs in `specs/` against the Gradle test results. It fails if
an ID has no block in `tasks.md`, a block has no requirement, `Validado` has
unchecked sub-items, or `Validado` has no passing test. It runs as part of
`check`.

## Rules

- Every requirement marked `Validado` has at least one passing test named after
  it. Pending requirements may have none yet.
- Mock ports (repository interfaces), never concrete adapters, in use-case tests.
- Use H2 with the test `application.yml` override for persistence tests.
- JaCoCo produces the report on every test run. The 80% rule lives in
  `jacocoTestCoverageVerification` and is wired into `check`, so
  `./gradlew build` fails below it.
