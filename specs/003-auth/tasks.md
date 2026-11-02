# Tasks: Auth

## AUTH01 — Register returns 201 with userId
- [x] `User` aggregate + `UserId` (`domain`)
- [x] `RegisterUserUseCase` (`application`)
- [x] `POST /auth/register` → `201` (`infrastructure`)
- [x] `users` table (`Liquibase` 000)
- [x] **Validado** — `auth01_*` (3 tests)

## AUTH02 — Email normalized and validated
- [x] `UserEmail` trims, lowercases, validates format and caps at 255 (`domain`)
- [x] `@NotBlank` on the request (`infrastructure`)
- [x] **Validado** — `auth02_*` (10 tests)

## AUTH03 — Unique email
- [x] `findByEmail` duplicate check → `409` (`application` + `infrastructure`)
- [x] **Validado** — `auth03_*` (2 tests)

## AUTH04 — BCrypt password hash
- [x] `BCryptPasswordHasher` (`infrastructure`)
- [x] Clear password never persisted (`application`)
- [x] **Validado** — `auth04_*` (3 tests)

## AUTH05 — Login returns a JWT
- [x] `LoginUserUseCase` (`application`)
- [x] `POST /auth/login` → `200` with token (`infrastructure`)
- [x] **Validado** — `auth05_*` (2 tests)

## AUTH06 — Same 401 for unknown email and wrong password
- [x] `UnauthenticatedException("Invalid credentials")` in both paths (`application`)
- [x] **Validado** — `auth06_*` (3 tests)

## AUTH07 — JWT subject and expiry
- [x] `JwtTokenGenerator` sets `sub` = userId and 24 h expiry (`infrastructure`)
- [x] `JwtTokenParser` reads the subject (`infrastructure`)
- [x] **Validado** — `auth07_*` (3 tests)

## AUTH08 — Name required, trimmed, max 100
- [x] `UserName` (`domain`)
- [x] `@NotBlank` on the request (`infrastructure`)
- [x] **Validado** — `auth08_*` (7 tests)

## AUTH09 — Password at least 8 characters
- [x] `Password` value object with a minimum of 8 (`domain`)
- [x] Validated in `RegisterUserUseCase` before hashing (`application`)
- [x] **Validado** — `auth09_*` (6 tests)
