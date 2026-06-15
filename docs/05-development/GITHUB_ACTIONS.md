# GitHub Actions

WearBrowser uses GitHub Actions as the public quality gate for every push and pull request.

## Workflow

File:

```text
.github/workflows/android.yml
```

Checks:

```bash
./gradlew detekt
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

## Local equivalent

Run the same gate locally before pushing:

```bash
./scripts/quality-gate.sh
```

Or run commands manually:

```bash
./gradlew detekt
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

## Required branch protection

Recommended GitHub settings for `master` / `main`:

- Require a pull request before merging.
- Require status checks to pass before merging.
- Require `Android CI / Quality Gate`.
- Require conversation resolution before merging.
- Do not allow force pushes.

## Artifact policy

CI uploads:

- Unit test reports
- Debug APK

Debug APKs are for internal validation only and are not official releases.
