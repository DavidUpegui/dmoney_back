# Auth

Email and password authentication for the API. The back-end issues a JWT that
identifies the user; every protected endpoint relies on it.

## Requirements

- **AUTH01** — A visitor can register with email, password and name; the
  response is `201` with the new `userId`.
- **AUTH02** — The email is trimmed, lowercased, must match an email format and
  is at most 255 characters; invalid input returns `400`.
- **AUTH03** — The email is unique; registering an existing email returns `409`.
- **AUTH04** — The password is hashed with BCrypt; the clear value is never
  stored.
- **AUTH05** — Valid credentials return `200` with a JWT.
- **AUTH06** — An unknown email and a wrong password return the same `401`
  "Invalid credentials"; the response does not reveal which one failed.
- **AUTH07** — The JWT identifies the user (`sub` = `userId`) and expires in
  24 hours.
- **AUTH08** — The name is required, trimmed and at most 100 characters;
  invalid input returns `400`.
- **AUTH09** — The password is at least 8 characters at registration; shorter
  values return `400`.

## API Contract

| Method | Path | Req | Success | Errors |
|--------|------|-----|---------|--------|
| `POST` | `/auth/register` | AUTH01, AUTH02, AUTH03, AUTH04, AUTH08, AUTH09 | `201` | `400`, `409` |
| `POST` | `/auth/login` | AUTH05, AUTH06, AUTH07 | `200` | `400`, `401` |

### Request / Response

```json
POST /auth/register
{ "email": "a@b.com", "password": "secret123", "name": "Alice" }
```

```json
201 Created
{ "userId": "uuid" }
```

```json
POST /auth/login
{ "email": "a@b.com", "password": "secret123" }
```

```json
200 OK
{ "token": "eyJhbGciOi..." }
```

All errors use the `ApiError` body (`status`, `error`, `message`, `timestamp`).

## Data Model

| Entity | Fields | Constraints |
|--------|--------|-------------|
| `users` | `id`, `email`, `password_hash`, `name`, `provider` | `id` UUID PK; unique `email`; all not null |

Value objects (immutable, self-validating):

- `UserEmail` — trimmed, lowercased, email format, max 255.
- `UserName` — trimmed, not blank, max 100.
- `Password` — registration input, at least 8 characters.
- `PasswordHash` — stored hash, not blank.

## Out of Scope

- Refresh tokens, logout and token revocation.
- Email verification and password reset.
- OAuth providers (`GOOGLE` exists in the enum but is not wired).
- Rate limiting and brute-force protection.
