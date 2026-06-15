# Release Strategy

WearBrowser uses milestone-driven releases instead of feature dumping.

## Release channels

| Channel | Meaning |
|---|---|
| `alpha` | Runnable but incomplete; used for early engineering validation. |
| `beta` | Feature-complete for the milestone; used for UX and device testing. |
| `rc` | Release candidate; only critical fixes are accepted. |
| stable | Public release with changelog, APK artifact and known limitations. |

## Version examples

```text
v0.1.0-alpha.1
v0.1.0-beta.1
v0.1.0-rc.1
v0.1.0
```

## Required release checks

Before creating a tag, run:

```bash
./scripts/quality-gate.sh
```

A release cannot be promoted unless these are true:

- Detekt passes.
- Unit tests pass.
- `assembleDebug` passes.
- Known limitations are documented.
- CHANGELOG is updated.
- Manual smoke testing is done on at least one emulator or physical watch.

## Creating a release candidate

```bash
scripts/create-release-candidate.sh v0.1.0-alpha.1
```

This runs the local quality gate, creates an annotated tag and pushes it. The GitHub Actions release workflow then builds the APK artifact.

## Release principle

WearBrowser should release small, verified increments. A release should improve trust, not just increase the version number.
