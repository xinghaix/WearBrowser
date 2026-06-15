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
