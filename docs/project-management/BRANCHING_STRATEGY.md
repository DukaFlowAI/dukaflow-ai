# Branching Strategy

## Permanent branches

### `main`

Contains stable, release-ready code.

### `develop`

Contains reviewed features that are being prepared for the next release.

## Temporary branches

- `feature/<issue-number>-<description>`
- `fix/<issue-number>-<description>`
- `docs/<issue-number>-<description>`
- `test/<issue-number>-<description>`
- `chore/<issue-number>-<description>`

## Merge targets

```text
feature/fix/docs/test/chore branch
              |
              v
           develop
              |
              v
             main
```

Feature branches must be created from the latest `develop` branch. Stable release pull requests are opened from `develop` into `main`.
