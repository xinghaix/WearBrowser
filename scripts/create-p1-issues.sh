#!/usr/bin/env bash
set -euo pipefail

REPO="${1:-xinghaix/WearBrowser}"
MILESTONE="${2:-P1 First Runnable Browser}"

if ! command -v gh >/dev/null 2>&1; then
  echo "GitHub CLI (gh) is required. Install it first: https://cli.github.com/"
  exit 1
fi

create_issue() {
  local title="$1"
  local labels="$2"
  local body="$3"

  gh issue create \
    --repo "$REPO" \
    --title "$title" \
    --milestone "$MILESTONE" \
    --label "$labels" \
    --body "$body"
}

create_issue \
  "P1: Harden address/search normalization" \
  "type: feature,area: browser-engine,priority: p1" \
  "## Goal\nMake URL/search input reliable.\n\n## Acceptance criteria\n- Plain domains become HTTPS URLs.\n- Full URLs load directly.\n- Search queries become search URLs.\n- Empty input is ignored.\n- Unit tests cover URL, domain, localhost, IP, and search query cases.\n\n## Validation\nRun ./gradlew testDebugUnitTest."

create_issue \
  "P1: Validate BrowserEngine smoke path" \
  "type: test,area: browser-engine,area: webview,priority: p1" \
  "## Goal\nVerify BrowserController -> BrowserEngine -> WebView callbacks.\n\n## Acceptance criteria\n- load() reaches engine.\n- URL, title, progress, canGoBack, and canGoForward update state.\n- Page errors create visible error state.\n\n## Validation\nManual emulator smoke test plus unit tests where possible."

create_issue \
  "P1: Implement minimal browser controls validation" \
  "type: feature,area: ui,area: webview,priority: p1" \
  "## Goal\nMake Back, Forward, Reload, Home, and 100% reset reliably usable.\n\n## Acceptance criteria\n- Back respects canGoBack.\n- Forward respects canGoForward.\n- Reload calls engine reload.\n- Home loads configured home URL.\n- 100% reset clamps zoom state correctly."

create_issue \
  "P1: Add first manual test matrix" \
  "type: docs,area: docs,priority: p1,good first issue" \
  "## Goal\nTrack manual validation for first runnable browser builds.\n\n## Acceptance criteria\n- Add checklist for install, launch, input, navigation, zoom, gestures, lifecycle, and downloads.\n- Include square and round screen sections.\n- Document known limitations before alpha release."

create_issue \
  "P1: Run first emulator install smoke test" \
  "type: test,area: ci,priority: p1" \
  "## Goal\nInstall the debug APK and verify the first runnable browser flow.\n\n## Acceptance criteria\n- assembleDebug succeeds.\n- APK installs.\n- App launches.\n- A HTTPS site loads.\n- Back / Forward / Reload can be validated.\n- Findings are documented in docs/07-sprints/IMPLEMENTATION_STATUS.md."

echo "P1 issues created for $REPO."
