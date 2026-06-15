# Sprint 1: Foundation Implementation

This sprint converts the product and architecture documents into concrete source-code building blocks.

## Goal

Move WearBrowser from documentation-heavy prototype toward a compilable, maintainable implementation.

## Implemented modules

### BrowserEngine

Added `com.wearbrowser.engine.BrowserEngine` as the stable browser abstraction.

Responsibilities:

- load URL
- back / forward / reload / stop
- zoom
- user-agent switching
- OLED black mode
- Reader Mode injection
- JavaScript execution
- snapshot capture
- download events

Current implementation:

- `WebViewBrowserEngine`

This allows future replacement with GeckoView or another engine without rewriting UI/product logic.

### BrowserController

Added `BrowserController` as the application-level command facade.

Responsibilities:

- normalize URLs
- back-or-home fallback
- zoom clamping
- edge zoom integration
- hide direct engine calls from UI

### GestureEngine

Added `GestureEngine` and `GestureAction`.

Responsibilities:

- resolve edge back
- resolve edge forward
- resolve top/bottom edge reveal
- resolve right-edge zoom
- keep gesture decision logic testable outside Compose

### ZoomPolicy

Added `ZoomPolicy`.

Responsibilities:

- 50%~300% clamp
- 1% precision normalization
- snap points
- edge-drag-to-zoom conversion

### TabPolicy

Added `TabPolicy`.

Responsibilities:

- Active / Warm / Sleeping / SnapshotOnly lifecycle transition
- predictable memory policy foundation

### WatchLayoutEngine

Added a first deterministic WLE profile generator.

Responsibilities:

- infer safe-area mode
- infer reader preference
- infer default zoom
- domain-aware layout profile

### Repository interfaces

Added repository interfaces for:

- bookmarks
- history
- site profile
- tabs

Current backing store remains `BrowserStore` / SharedPreferences. Room can replace this later behind the same contracts.

### Design Tokens

Added first-class design tokens:

- spacing
- radius
- motion
- opacity

## Tests added

- `BrowserControllerTest`
- `GestureEngineTest`
- `ZoomPolicyTest`

These tests cover the first non-UI deterministic core behaviors.

## Current limitation

The new abstractions are introduced and partially integrated, but the existing `BrowserState` path still uses direct WebView references in several places. The next sprint should replace those direct dependencies with `BrowserController` and `BrowserEngine` end-to-end.

## Next sprint

Sprint 2 should focus on:

1. Full BrowserEngine integration in `WearWebView` and `BrowserState`.
2. Replace direct Compose gesture branching with `GestureEngine`.
3. Move tab lifecycle logic to `TabPolicy`.
4. Move site-profile access behind repositories.
5. Run Android compile and fix real compiler errors.
