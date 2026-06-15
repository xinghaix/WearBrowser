# Sprint 4 — Build Readiness and Lifecycle Safety

## Goal

Move WearBrowser from a feature prototype toward a lifecycle-safe Android product baseline.

Sprint 4 focuses on the parts that usually break real Android browser shells:

- WebView pause/resume handling.
- WebView destruction and native resource cleanup.
- Controller-level lifecycle forwarding.
- Tab aging through a single policy object.
- Build metadata alignment.
- Additional unit-test coverage around lifecycle and tab policy.

## Implemented

### BrowserEngine lifecycle contract

`BrowserEngine` now exposes optional lifecycle methods:

```kotlin
fun onPause()
fun onResume()
fun destroy()
```

UI code still depends only on the engine abstraction. The WebView implementation owns the native WebView cleanup details.

### WebViewBrowserEngine lifecycle handling

`WebViewBrowserEngine` now performs:

- `webView.onPause()`
- `webView.pauseTimers()`
- `webView.onResume()`
- `webView.resumeTimers()`
- `stopLoading()` during teardown
- client/listener detachment
- view removal
- `webView.destroy()`

The implementation is defensive and idempotent enough for Android lifecycle churn.

### BrowserController lifecycle facade

`BrowserController` forwards lifecycle operations to the engine:

```kotlin
controller.onPause()
controller.onResume()
controller.destroy()
```

This preserves the architectural rule: UI and state layers do not directly operate on WebView.

### Compose lifecycle binding

`BrowserEngineView` observes the host lifecycle through `LocalLifecycleOwner`:

- `ON_PAUSE` → pause engine and persist tab state.
- `ON_RESUME` → resume engine and re-apply settings.
- `ON_DESTROY` / disposal → release engine resources.

### TabPolicy integration

`BrowserState.ageTabs()` now delegates lifecycle decisions to `TabPolicy` using product-level constants from `PerformancePolicy`.

This removes duplicated timing rules from UI state and makes tab aging testable.

### Tests

Added coverage for:

- Controller lifecycle forwarding.
- TabPolicy state progression.

## Remaining before true build readiness

Sprint 4 improves source quality, but true build readiness still requires an Android SDK run:

```bash
./gradlew clean test assembleDebug
```

Expected follow-up work:

1. Fix compile errors discovered by Android Gradle Plugin.
2. Run Detekt and resolve violations.
3. Add instrumentation tests for WebView lifecycle.
4. Verify lifecycle behavior on real Wear OS devices.
5. Tune WebView memory behavior under tab switching.

## Status

`1.3.3-foundation-sprint-4` is still an implementation sprint build, not a release build.
