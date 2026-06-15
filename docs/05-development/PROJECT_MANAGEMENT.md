# Project Management

WearBrowser is managed as a product-grade open-source project. Work is split by milestones, labels, and issue types so the project can grow without losing engineering discipline.

## Milestones

### P0 Foundation

Goal: keep the repository buildable, testable, and governable.

Scope:

- Quality gate
- GitHub Actions
- Branch protection
- ADR foundation
- Contribution workflow
- Release foundation

### P1 First Runnable Browser

Goal: make the browser runnable and useful on an Android device or emulator.

Scope:

- Address/search bar
- WebView navigation
- Back / forward / reload
- Basic zoom
- Basic download callback
- Error page
- First run smoke test

### P2 Watch-First Interaction

Goal: make the app feel like a watch-native browser.

Scope:

- Edge Back
- Edge Forward
- Edge Zoom
- Edge Pie Menu
- Round Safe Area
- Immersive Dock
- Gesture onboarding

### P3 Product Quality

Goal: prepare for alpha/beta releases.

Scope:

- UI polish
- Performance budget
- Tab sleep validation
- Reader mode hardening
- Manual test matrix
- Release checklist

### v1.0 Release

Goal: publish the first stable release.

Scope:

- CI green
- APK release
- Basic documentation complete
- Tested on square and round devices
- Known limitations documented

## Label taxonomy

### Type labels

- `type: bug`
- `type: build`
- `type: docs`
- `type: feature`
- `type: refactor`
- `type: test`
- `type: ux`

### Area labels

- `area: browser-engine`
- `area: gesture-engine`
- `area: webview`
- `area: ui`
- `area: round-screen`
- `area: zoom`
- `area: reader`
- `area: tabs`
- `area: ci`
- `area: docs`

### Priority labels

- `priority: p0`
- `priority: p1`
- `priority: p2`
- `priority: p3`

### Contributor labels

- `good first issue`
- `help wanted`

## Issue quality bar

Every implementation issue should include:

1. Problem statement
2. Expected behavior
3. Acceptance criteria
4. Test or validation plan
5. Affected modules

## Pull request quality bar

Every PR should satisfy:

1. It passes `./scripts/quality-gate.sh`.
2. It does not bypass `BrowserEngine`, `GestureEngine`, or repository abstractions.
3. It updates docs when behavior changes.
4. It protects experimental features with feature flags.
5. It includes tests for non-trivial logic.

## Current planning focus

The current planning focus is **P1 First Runnable Browser**. The project should now move from foundation/governance into a minimal runnable browser that can be installed, opened, and used for a basic browsing session.
