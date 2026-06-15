#!/usr/bin/env bash
set -euo pipefail

# WearBrowser P0-3: GitHub project governance
# Run from the repository root.

if [ ! -f "settings.gradle.kts" ] || [ ! -d ".git" ]; then
  echo "ERROR: run this script from the WearBrowser repository root." >&2
  exit 1
fi

mkdir -p .github/ISSUE_TEMPLATE .github/workflows docs/05-development scripts

cat > .github/ISSUE_TEMPLATE/config.yml <<'YAML'
blank_issues_enabled: false
contact_links:
  - name: Product and architecture discussion
    url: https://github.com/xinghaix/WearBrowser/discussions
    about: Use Discussions for open-ended product, architecture, and roadmap conversations.
YAML

cat > .github/ISSUE_TEMPLATE/bug_report.yml <<'YAML'
name: Bug report
description: Report a reproducible problem in WearBrowser.
title: "bug: "
labels: ["type: bug", "needs: triage"]
body:
  - type: markdown
    attributes:
      value: |
        Thanks for helping improve WearBrowser. Please keep the report reproducible.
  - type: textarea
    id: summary
    attributes:
      label: Summary
      description: What happened?
      placeholder: Describe the bug clearly.
    validations:
      required: true
  - type: textarea
    id: steps
    attributes:
      label: Steps to reproduce
      value: |
        1.
        2.
        3.
    validations:
      required: true
  - type: textarea
    id: expected
    attributes:
      label: Expected behavior
    validations:
      required: true
  - type: textarea
    id: actual
    attributes:
      label: Actual behavior
    validations:
      required: true
  - type: input
    id: device
    attributes:
      label: Device / emulator
      placeholder: Pixel Watch, Galaxy Watch, Wear OS emulator, Android phone, etc.
  - type: input
    id: android
    attributes:
      label: Android / Wear OS version
  - type: textarea
    id: logs
    attributes:
      label: Logs / screenshots
      render: text
YAML

cat > .github/ISSUE_TEMPLATE/build_failure.yml <<'YAML'
name: Build failure
description: Report Gradle, Kotlin, Detekt, CI, or local quality-gate failures.
title: "build: "
labels: ["type: build", "needs: triage"]
body:
  - type: textarea
    id: command
    attributes:
      label: Command
      placeholder: ./scripts/quality-gate.sh
    validations:
      required: true
  - type: textarea
    id: output
    attributes:
      label: Full error output
      description: Paste the complete relevant output, not only the final line.
      render: text
    validations:
      required: true
  - type: input
    id: java
    attributes:
      label: Java version
      placeholder: java --version
  - type: input
    id: os
    attributes:
      label: OS
      placeholder: macOS, Linux, Windows
YAML

cat > .github/ISSUE_TEMPLATE/feature_request.yml <<'YAML'
name: Feature request
description: Propose a concrete user-facing feature.
title: "feat: "
labels: ["type: feature", "needs: triage"]
body:
  - type: textarea
    id: problem
    attributes:
      label: Problem
      description: What watch-first problem does this solve?
    validations:
      required: true
  - type: textarea
    id: proposal
    attributes:
      label: Proposal
      description: Describe the desired behavior.
    validations:
      required: true
  - type: checkboxes
    id: principles
    attributes:
      label: Product fit
      options:
        - label: This improves a watch-first use case, not only a phone-style browser feature.
        - label: This keeps UI minimal and content-first.
        - label: This can be guarded by a feature flag if experimental.
YAML

cat > .github/ISSUE_TEMPLATE/ux_feedback.yml <<'YAML'
name: UX feedback
description: Report friction in gestures, round-screen layout, zoom, or visual design.
title: "ux: "
labels: ["area: ux", "needs: triage"]
body:
  - type: dropdown
    id: area
    attributes:
      label: Area
      options:
        - Edge Back
        - Edge Zoom
        - Pie Menu
        - Round Safe Area
        - Reader Mode
        - Dock / Address Bar
        - Theme / Visual Design
        - Other
    validations:
      required: true
  - type: textarea
    id: feedback
    attributes:
      label: Feedback
      description: What felt slow, confusing, ugly, too small, too large, or hard to use?
    validations:
      required: true
  - type: textarea
    id: suggestion
    attributes:
      label: Suggested improvement
YAML

cat > .github/pull_request_template.md <<'MARKDOWN'
## Summary

Describe what changed and why.

## Type

- [ ] Bug fix
- [ ] Feature
- [ ] Refactor
- [ ] Documentation
- [ ] Build / CI
- [ ] Product / UX

## Quality gate

- [ ] `./scripts/quality-gate.sh` passes locally
- [ ] Unit tests added or updated where appropriate
- [ ] No direct UI dependency on raw WebView unless justified
- [ ] Experimental behavior is protected by a feature flag
- [ ] Documentation updated when architecture, behavior, or product scope changed

## Watch-first checklist

- [ ] Works for small screens
- [ ] Does not make round-screen usage worse
- [ ] Does not add unnecessary persistent chrome
- [ ] Gesture behavior avoids common scroll/zoom conflicts

## Screenshots / recordings

Add screenshots, video, or GIFs for visual or interaction changes.
MARKDOWN

cat > .github/labeler.yml <<'YAML'
area: docs:
  - changed-files:
      - any-glob-to-any-file:
          - docs/**
          - README.md
          - ROADMAP.md
          - CONTRIBUTING.md
area: build:
  - changed-files:
      - any-glob-to-any-file:
          - .github/**
          - build.gradle.kts
          - settings.gradle.kts
          - gradle.properties
          - app/build.gradle.kts
          - scripts/**
area: browser-engine:
  - changed-files:
      - any-glob-to-any-file:
          - app/src/main/java/**/engine/**
          - app/src/main/java/**/browser/**
area: gesture:
  - changed-files:
      - any-glob-to-any-file:
          - app/src/main/java/**/gesture/**
area: ui:
  - changed-files:
      - any-glob-to-any-file:
          - app/src/main/java/**/ui/**
          - app/src/main/java/**/design/**
area: tests:
  - changed-files:
      - any-glob-to-any-file:
          - app/src/test/**
          - app/src/androidTest/**
YAML

cat > .github/workflows/labeler.yml <<'YAML'
name: Pull Request Labeler

on:
  pull_request_target:
    types: [opened, synchronize, reopened]

permissions:
  contents: read
  pull-requests: write

jobs:
  label:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/labeler@v5
YAML

cat > .github/release-drafter.yml <<'YAML'
name-template: 'v$RESOLVED_VERSION'
tag-template: 'v$RESOLVED_VERSION'
categories:
  - title: 'Features'
    labels:
      - 'type: feature'
  - title: 'Bug fixes'
    labels:
      - 'type: bug'
  - title: 'Build and CI'
    labels:
      - 'type: build'
      - 'area: build'
  - title: 'Documentation'
    labels:
      - 'area: docs'
  - title: 'Maintenance'
    labels:
      - 'type: refactor'
      - 'type: chore'
change-template: '- $TITLE @$AUTHOR (#$NUMBER)'
change-title-escapes: '\\<*_&'
version-resolver:
  major:
    labels:
      - 'release: major'
  minor:
    labels:
      - 'release: minor'
  patch:
    labels:
      - 'release: patch'
  default: patch
template: |
  ## Changes

  $CHANGES

  ## Quality

  - CI quality gate must pass before publishing.
  - Android APK should be manually smoke-tested before release.
YAML

cat > .github/workflows/release-drafter.yml <<'YAML'
name: Release Drafter

on:
  push:
    branches:
      - master
  pull_request:
    types: [closed]

permissions:
  contents: read

jobs:
  update_release_draft:
    if: github.event_name == 'push' || github.event.pull_request.merged == true
    runs-on: ubuntu-latest
    permissions:
      contents: write
      pull-requests: read
    steps:
      - uses: release-drafter/release-drafter@v6
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
YAML

cat > .github/dependabot.yml <<'YAML'
version: 2
updates:
  - package-ecosystem: "gradle"
    directory: "/"
    schedule:
      interval: "weekly"
    open-pull-requests-limit: 5
    labels:
      - "type: dependency"
      - "area: build"
  - package-ecosystem: "github-actions"
    directory: "/"
    schedule:
      interval: "weekly"
    open-pull-requests-limit: 5
    labels:
      - "type: dependency"
      - "area: build"
YAML

cat > CODEOWNERS <<'TEXT'
# Default ownership for the whole repository.
* @xinghaix

# Product and architecture docs should be reviewed carefully.
/docs/ @xinghaix
/README.md @xinghaix
/ROADMAP.md @xinghaix

# CI and build infrastructure.
/.github/ @xinghaix
/build.gradle.kts @xinghaix
/settings.gradle.kts @xinghaix
/gradle.properties @xinghaix
TEXT

cat > docs/05-development/GITHUB_GOVERNANCE.md <<'MARKDOWN'
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
MARKDOWN

cat > scripts/setup-github-governance.sh <<'BASH'
#!/usr/bin/env bash
set -euo pipefail

# Optional helper. Requires GitHub CLI authentication:
#   gh auth login
# Run from the repository root.

if ! command -v gh >/dev/null 2>&1; then
  echo "GitHub CLI 'gh' is required for this optional setup." >&2
  exit 1
fi

REPO="xinghaix/WearBrowser"

create_label() {
  local name="$1"
  local color="$2"
  local description="$3"
  gh label create "$name" --repo "$REPO" --color "$color" --description "$description" --force >/dev/null
}

create_label "needs: triage" "ededed" "Needs maintainer triage"
create_label "type: bug" "d73a4a" "Something is broken"
create_label "type: feature" "0e8a16" "New user-facing feature"
create_label "type: build" "5319e7" "Build, Gradle, CI, or tooling"
create_label "type: dependency" "0366d6" "Dependency updates"
create_label "type: refactor" "c5def5" "Code refactoring without behavior change"
create_label "type: chore" "fef2c0" "Maintenance work"

create_label "area: browser-engine" "1d76db" "BrowserEngine or WebView ownership"
create_label "area: gesture" "fbca04" "Gestures, pie menu, edge interactions"
create_label "area: ui" "c2e0c6" "Visual UI, Compose, design system"
create_label "area: docs" "0075ca" "Documentation"
create_label "area: build" "5319e7" "Build and CI"
create_label "area: performance" "0052cc" "Performance and memory"
create_label "area: reader" "7057ff" "Reader mode"
create_label "area: wle" "b60205" "Watch Layout Engine"
create_label "area: ux" "fbca04" "User experience"

create_label "priority: p0" "b60205" "Critical path"
create_label "priority: p1" "d93f0b" "High priority"
create_label "priority: p2" "fbca04" "Medium priority"
create_label "priority: p3" "cfd3d7" "Low priority"

create_label "status: blocked" "b60205" "Blocked by another issue or decision"
create_label "status: ready" "0e8a16" "Ready to implement"
create_label "status: in-progress" "fbca04" "Currently being worked on"

create_label "release: major" "b60205" "Major version bump"
create_label "release: minor" "1d76db" "Minor version bump"
create_label "release: patch" "0e8a16" "Patch version bump"

# Milestones are not idempotent with --force, so create only if missing.
for milestone in \
  "P0 Foundation" \
  "P1 First Runnable Browser" \
  "P2 Watch-First Interaction" \
  "P3 Product Quality" \
  "v1.0 Release"; do
  if ! gh api "repos/$REPO/milestones" --jq '.[].title' | grep -Fxq "$milestone"; then
    gh api "repos/$REPO/milestones" -f title="$milestone" >/dev/null
  fi
done

echo "GitHub labels and milestones configured for $REPO."
BASH
chmod +x scripts/setup-github-governance.sh

# Insert governance section into README if not present.
if [ -f README.md ] && ! grep -q "## Governance" README.md; then
  cat >> README.md <<'MARKDOWN'

## Governance

WearBrowser uses a product-grade open-source workflow:

- Pull requests must pass the quality gate.
- Issues use structured templates for bugs, build failures, feature requests, and UX feedback.
- Architecture-level changes should be documented in ADRs.
- Experimental behavior should be protected by feature flags.
- Release notes are drafted from merged pull requests.

See `docs/05-development/GITHUB_GOVERNANCE.md` for details.
MARKDOWN
fi

echo "P0-3 GitHub governance files installed."
echo "Next: git add . && git commit -m 'chore: add GitHub governance workflow'"
echo "Optional: scripts/setup-github-governance.sh # requires gh auth login"
