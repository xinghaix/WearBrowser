# ADR-0001: Use Android WebView as the initial browser engine

Status: Accepted

Date: 2026-06-15

## Context

WearBrowser needs a browser engine that can run on Android watches with minimal packaging complexity, acceptable compatibility, and a low barrier for contributors.

Alternatives considered:

- Android WebView
- GeckoView
- Bundled Chromium-based engine
- Custom lightweight renderer

## Decision

Use Android WebView as the initial engine implementation.

The public browser abstraction remains `BrowserEngine`, so the product is not permanently coupled to WebView.

## Consequences

Positive:

- Smaller APK and simpler distribution.
- Easier first runnable version.
- Uses the WebView implementation already available on Android devices.
- Allows the team to focus on watch-first interaction, safe area, gestures, and layout optimization.

Negative:

- Web compatibility and performance depend on the device WebView provider.
- Some advanced browser features are constrained by WebView APIs.
- Engine-specific behavior must be isolated carefully.

## Follow-up

Keep all direct WebView access inside `WebViewBrowserEngine` and WebView-specific UI integration code. UI and domain logic must call `BrowserEngine` / `BrowserController` instead of directly manipulating WebView.
