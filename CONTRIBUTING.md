# Contributing to DukaFlow AI

## Permanent branches

- `main`: stable, release-ready code
- `develop`: integrated development code

Do not implement features directly on either permanent branch.

## Branch naming

Use one of these formats:

```text
feature/<issue-number>-<description>
fix/<issue-number>-<description>
docs/<issue-number>-<description>
test/<issue-number>-<description>
chore/<issue-number>-<description>
```

Example:

```text
feature/12-product-management
```

## Development workflow

1. Create or select a GitHub issue.
2. Update your local `develop` branch.
3. Create a new branch from `develop`.
4. Implement one focused task.
5. Run relevant tests and formatting checks.
6. Commit using a clear message.
7. Push the branch to GitHub.
8. Open a pull request targeting `develop`.
9. Request review from the other contributor.
10. Resolve all review comments before merging.

## Commit-message examples

```text
feat: add product category entity
fix: prevent negative inventory quantities
docs: add authentication flow diagram
test: add purchase service unit tests
chore: configure editor settings
```

## Pull-request requirements

Every pull request should:

- Reference its GitHub issue
- Explain what changed
- Explain how the change was tested
- Include screenshots for visible interface changes
- Avoid unrelated modifications
- Contain no passwords, tokens or private credentials

## Secrets

Never commit:

- `.env`
- Database passwords
- JWT secrets
- M-Pesa credentials
- Email passwords
- Production API keys
- Private certificates

Use `.env.example` to document required variable names without real values.

## Code review

The reviewer should check:

- Correctness
- Security
- Readability
- Test coverage
- Documentation
- Compatibility with existing modules

The feature owner should not merge unresolved review comments.
