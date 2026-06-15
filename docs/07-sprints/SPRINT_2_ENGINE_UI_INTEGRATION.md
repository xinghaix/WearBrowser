# Sprint 2: BrowserEngine UI Integration

## Goal

Move the visible browser surface away from direct WebView operations and into the `BrowserEngine + BrowserController` path.

This is the first implementation step that turns the architectural rule "UI must not operate WebView directly" into concrete code.

## Implemented

- Added `BrowserEngineView` as the Compose host for the engine-backed browser surface.
- Connected `WebViewBrowserEngine` to `BrowserController` during AndroidView creation.
- Attached a `BrowserEngineListener` that updates `BrowserState` for:
  - page start
  - progress
  - page finish
  - navigation state
  - download requests
  - main-frame errors
- Updated `BrowserScreen` to render `BrowserEngineView` instead of the legacy raw WebView composable.
- Refactored `BrowserState` browser commands to call `BrowserController`:
  - load
  - back/home
  - forward
  - reload
  - stop loading
  - zoom
  - user agent
  - OLED black
  - Reader Mode
  - cache clearing
- Kept `WearWebView` as a compatibility shim that delegates to `BrowserEngineView`.

## Product Impact

The product is now closer to the intended architecture:

```text
Compose UI
   ↓
BrowserState
   ↓
BrowserController
   ↓
BrowserEngine
   ↓
Android WebView
```

This makes future engine replacement, gesture testing, and state management significantly cleaner.

## Remaining Work

- Run an Android build and fix compiler issues in a real Android SDK environment.
- Move `BrowserState` into a ViewModel-style state holder.
- Replace in-memory/prefs-backed storage with Room repositories.
- Add lifecycle-aware engine detach/destroy handling.
- Add instrumentation tests for the engine-backed browser view.
