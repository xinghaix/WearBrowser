#!/usr/bin/env bash
set -euo pipefail

REPO="${1:-xinghaix/WearBrowser}"

if ! command -v gh >/dev/null 2>&1; then
  echo "GitHub CLI (gh) is required. Install it first: https://cli.github.com/"
  exit 1
fi

ensure_label() {
  local name="$1"
  local color="$2"
  local description="$3"
  gh label create "$name" --repo "$REPO" --color "$color" --description "$description" --force >/dev/null
}

ensure_milestone() {
  local title="$1"
  local description="$2"
  if ! gh api "repos/$REPO/milestones" --jq '.[].title' | grep -Fxq "$title"; then
    gh api --method POST "repos/$REPO/milestones" \
      -f title="$title" \
      -f description="$description" >/dev/null
  fi
}

ensure_label "type: bug" "d73a4a" "Something is broken"
ensure_label "type: build" "b60205" "Build, Gradle, CI, SDK, or environment issue"
ensure_label "type: docs" "0075ca" "Documentation work"
ensure_label "type: feature" "0e8a16" "New user-facing or engineering feature"
ensure_label "type: refactor" "5319e7" "Internal code structure improvement"
ensure_label "type: test" "1d76db" "Testing or validation work"
ensure_label "type: ux" "fbca04" "Interaction, layout, or visual design feedback"

ensure_label "area: browser-engine" "5319e7" "BrowserEngine and BrowserController"
ensure_label "area: gesture-engine" "5319e7" "Gesture recognition, dispatch, and feedback"
ensure_label "area: webview" "5319e7" "Android WebView integration"
ensure_label "area: ui" "5319e7" "Compose UI and controls"
ensure_label "area: round-screen" "5319e7" "Round display support"
ensure_label "area: zoom" "5319e7" "Zoom model and controls"
ensure_label "area: reader" "5319e7" "Reader Mode and WLE"
ensure_label "area: tabs" "5319e7" "Tab lifecycle and tab manager"
ensure_label "area: ci" "5319e7" "CI, quality gate, release automation"
ensure_label "area: docs" "5319e7" "Documentation"

ensure_label "priority: p0" "b60205" "Critical foundation work"
ensure_label "priority: p1" "fbca04" "Important near-term work"
ensure_label "priority: p2" "c5def5" "Medium priority"
ensure_label "priority: p3" "ededed" "Low priority"
ensure_label "good first issue" "7057ff" "Good for new contributors"
ensure_label "help wanted" "008672" "Maintainer wants help"

ensure_milestone "P0 Foundation" "Buildable, testable, governable repository foundation."
ensure_milestone "P1 First Runnable Browser" "Installable browser with URL/search input and basic navigation."
ensure_milestone "P2 Watch-First Interaction" "Edge gestures, round screen, immersive controls, and watch-native browsing."
ensure_milestone "P3 Product Quality" "UI polish, performance, validation matrix, and alpha readiness."
ensure_milestone "v1.0 Release" "First stable release."

echo "Labels and milestones are ready for $REPO."
