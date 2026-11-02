# Capabilities

The bounded capabilities of the back-end and the spec that covers each one.
`requirementsCheck` reads this table: entries without a spec are reported on
every build so the gap stays visible.

| Capability | Context | Spec | State |
|---|---|---|---|
| auth | `auth` | `specs/003-auth/` | modeled |
| categories | `tracker` | `specs/004-categories/` | modeled |
| movements | `tracker` | `specs/001-movement/` | modeled |
| tags | `tracker` | `specs/002-tags/` | modeled |

- **modeled** — requirements with IDs, audited by `requirementsCheck`.
- **legacy** — predates the requirement model; skipped until retrofitted.
- **missing** — no spec yet; the capability is implemented but undocumented.

Adding a capability means adding a row here. Creating a spec folder that is not
listed in this table fails the audit.
