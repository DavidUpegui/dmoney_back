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
  (domain ↔ entity) and query correctness.
- **Security** — integration test asserting unauthenticated requests return
  `401` and cross-user access returns `404`.

## Rules

- Every acceptance criterion in `specs/` maps to at least one test.
- Mock ports (repository interfaces), never concrete adapters, in use-case tests.
- Use H2 with the test `application.yml` override for persistence tests.
- JaCoCo coverage must be ≥ 80% (enforced by `jacocoTestCoverageVerification`).
