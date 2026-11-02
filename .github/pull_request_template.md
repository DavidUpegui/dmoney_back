## What

<!-- What changed, in one or two sentences. -->

## Why

<!-- The problem this solves. Link the requirement if one covers it. -->

## Requirement

<!-- Requirement ID(s) and the spec.md / tasks.md block they belong to. -->

Req:

## Contract impact

<!-- None, or the linked dmoney_platform contract PR and what changed on the wire. -->

Contract:

## Verification

- [ ] `./gradlew check` green (tests, JaCoCo ≥ 80%, requirements audit)
- [ ] `Validado` checked in `tasks.md` if this closes a requirement
- [ ] Liquibase: new changeset added, no applied changeset edited
- [ ] `contracts/API_CONTRACT.md` and `docs/api.md` updated if the API surface changed
