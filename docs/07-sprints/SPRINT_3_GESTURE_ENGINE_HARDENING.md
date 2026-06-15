# Sprint 3 — Gesture Engine Hardening

## Goal

Move watch-first interactions from ad-hoc Compose gesture handlers toward a policy-driven, testable GestureEngine path.

This sprint focuses on the product-critical interaction layer:

- global left-edge back
- right-edge forward
- right-edge one-finger zoom
- top/bottom chrome reveal
- edge long-press Pie Menu
- future haptic/accessibility hooks

## Completed

### Gesture policy

- Expanded `GestureConfig` with horizontal slop, dominance ratio, pie-menu edge width, and zoom inertia constants.
- Gesture recognition now distinguishes horizontal and vertical dominance to reduce accidental triggers during page scrolling.
- Top/bottom edge gestures now share the same recognizer path as side gestures.

### Pure GestureEngine

- `GestureEngine` remains a pure Kotlin class with no Android, Compose, WebView, or UI dependencies.
- Added recognition APIs:
  - `resolveEnd`
  - `resolveDrag`
  - `resolveLongPress`
  - `resolveSettle`

### GestureDispatcher

- Added `GestureDispatcher` and `GestureActionSink`.
- Compose now dispatches recognized actions to the state/controller layer through an action sink instead of hardcoding all gesture policy inline.
- This creates a future integration point for haptics, accessibility announcements, analytics-free debug traces, and feature flags.

### Edge Pie Menu model

- Added `PieMenuAction`, `PieMenuItem`, and `PieMenuModel`.
- UI renders from the model instead of hardcoded item logic.
- Pie Menu now includes Reader Mode as a first-class action.

### BrowserState integration

- Added `applyEdgeZoom` for central zoom behavior.
- Added `hideChrome` for chrome dismissal.
- Added `onPieMenuAction` so menu actions remain centralized and testable.

### Tests

Added gesture unit tests for:

- left-edge back
- right-edge forward
- vertical-drift cancellation
- right-edge zoom stream
- bottom-edge dock reveal
- edge long-press pie menu

## Not Done Yet

- Android SDK compile validation.
- Real-device gesture tuning.
- Haptic feedback integration.
- Accessibility announcements.
- Visual drag-progress indicators for edge back/forward.
- Snapshot tests for Pie Menu layout.

## Next Sprint

**Sprint 4 — Build Readiness and Lifecycle Safety**

Target:

- run or prepare `assembleDebug`
- fix compile issues discovered by Android toolchain
- add Gradle wrapper if possible
- make BrowserEngine disposal lifecycle-safe
- reduce direct mutable state exposure in `BrowserState`
