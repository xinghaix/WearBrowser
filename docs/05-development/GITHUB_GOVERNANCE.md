# GitHub Governance

WearBrowser is a product-grade open-source project. GitHub is used not only as a code host, but as the public coordination layer for product, architecture, quality, and release work.

## Required quality gate

Every pull request must pass:

```bash
./scripts/quality-gate.sh
```

The gate currently covers:

- Detekt static analysis
- Debug unit tests
- Debug APK assembly

## Issue workflow

Use structured issue templates:

- Bug report
- Build failure
- Feature request
- UX feedback

Every issue should start with `needs: triage`. After triage, assign area labels, priority labels, and milestone when appropriate.

## Pull request rules

A PR should answer:

1. Does this improve the watch-first experience?
2. Does it preserve the content-first and minimal-UI philosophy?
3. Is the change routed through the correct architecture layer?
4. Is experimental behavior guarded by a feature flag?
5. Are tests and docs updated when needed?

## Labels

Recommended label groups:

- `type:*` — bug, feature, build, dependency, refactor, chore
- `area:*` — browser-engine, gesture, ui, docs, build, performance, reader, wle
- `priority:*` — p0, p1, p2, p3
- `status:*` — blocked, ready, in-progress
- `release:*` — major, minor, patch

## Milestones

Recommended milestones:

- `P0 Foundation` — compile, CI, quality gate, governance
- `P1 First Runnable Browser` — URL entry, WebView, back/forward/refresh
- `P2 Watch-First Interaction` — edge back, edge zoom, pie menu, immersive controls
- `P3 Product Quality` — UI polish, performance, stability, testing
- `v1.0 Release` — stable APK, docs, release checklist, F-Droid readiness

## Branch protection

For `master`, enable:

- Require pull request before merging
- Require status checks to pass
- Require branches to be up to date before merging
- Include administrators when practical
- Disallow force pushes

## Release process

Release Drafter maintains a draft release from merged PRs. Before publishing a release:

1. Run local quality gate.
2. Confirm GitHub Actions is green.
3. Build APK.
4. Smoke-test on emulator or watch.
5. Update `CHANGELOG.md` if needed.
6. Publish GitHub Release.
