# Changelog

## 1.3.3-foundation-sprint-4

### Added

- Added BrowserEngine lifecycle contract for pause, resume and destroy.
- Added WebViewBrowserEngine lifecycle-safe resource cleanup.
- Added BrowserController lifecycle forwarding.
- Added Compose lifecycle binding in BrowserEngineView.
- Added BrowserState host pause/resume/release hooks.
- Added lifecycle and tab policy unit tests.
- Added Sprint 4 implementation documentation.

### Changed

- Tab aging now delegates to TabPolicy and PerformancePolicy instead of duplicating constants inside BrowserState.
- Version updated to 1.3.3-foundation-sprint-4.

### Known limitations

- Android SDK compile validation has not been run in this environment.
- Real WebView lifecycle behavior still needs Wear OS device validation.


## 1.3.2-foundation-sprint-3

### Added

- Hardened pure `GestureEngine` recognition for drag, end, long-press and settle phases.
- Added `GestureDispatcher` and `GestureActionSink` as the product action boundary.
- Added model-driven Edge Pie Menu actions with Reader Mode support.
- Added gesture tests for edge back, forward, zoom, dock reveal and pie menu.

### Changed

- BrowserScreen now routes edge gestures through the dispatcher instead of embedding recognition policy inline.
- BrowserState now centralizes edge zoom, chrome hiding and pie menu action handling.

### Known limitations

- Android SDK compile validation has not been run in this environment.
- Haptic feedback and real-device gesture tuning are still pending.


## 1.3.1-foundation-sprint-2

- Integrated the visible browser surface with `BrowserEngineView`.
- Routed browser commands through `BrowserController` instead of direct WebView calls.
- Added engine listener wiring for progress, navigation, downloads, errors, and page changes.
- Kept the old `WearWebView` as a compatibility shim.
- Added Sprint 2 implementation documentation.


## Unreleased - Foundation Implementation Sprint 1

- Added BrowserEngine abstraction and WebViewBrowserEngine implementation.
- Added BrowserController command facade.
- Added testable GestureEngine and GestureAction model.
- Added ZoomPolicy for 50%~300% clamp, snap points, and edge zoom conversion.
- Added TabPolicy for Active/Warm/Sleeping/SnapshotOnly lifecycle decisions.
- Added WatchLayoutEngine profile generator.
- Added repository interfaces for bookmarks, history, site profiles, and tabs.
- Added design tokens for spacing, radius, motion, and opacity.
- Added foundation unit tests and sprint implementation status docs.
