# Branch Protection

WearBrowser treats `master` as the always-buildable integration branch.

## Required checks

The following check must pass before code is merged:

- `Build, test and static analysis`

This check runs:

```bash
./gradlew --no-daemon detekt
./gradlew --no-daemon testDebugUnitTest
./gradlew --no-daemon assembleDebug
```

## Recommended GitHub settings

Repository maintainers should enable these settings for `master`:

- Require status checks before merging.
- Require branches to be up to date before merging.
- Disallow force pushes.
- Disallow branch deletion.
- Prefer pull requests for non-trivial changes.

## Setup script

Maintainers with GitHub CLI access can run:

```bash
scripts/setup-branch-protection.sh xinghaix/WearBrowser master
```

If the script fails because the required check does not exist yet, run the Android Quality Gate workflow once first.
