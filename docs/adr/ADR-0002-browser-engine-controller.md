# ADR-0002: Route browser operations through BrowserEngine and BrowserController

Status: Accepted

Date: 2026-06-15

## Context

A browser app can quickly become difficult to maintain if UI code directly owns WebView instances, lifecycle, navigation, zoom, and JavaScript execution.

WearBrowser also wants long-term engine replaceability.

## Decision

All browser operations must go through:

```text
UI / Gesture
    ↓
BrowserController
    ↓
BrowserEngine
    ↓
WebViewBrowserEngine
```

`BrowserController` coordinates product-level commands. `BrowserEngine` defines the engine contract. `WebViewBrowserEngine` is the Android WebView implementation.

## Consequences

Positive:

- UI does not depend on WebView implementation details.
- Gesture actions can be tested separately from WebView.
- Engine replacement remains possible.
- Lifecycle management is centralized.

Negative:

- More interfaces and indirection than a simple WebView demo.
- Early development has more boilerplate.

## Rules

- Compose UI must not call `webView.goBack()` or equivalent directly.
- New browser capabilities should first be represented in `BrowserEngine` or controller-level actions.
- Engine callbacks should flow upward through listener/state mechanisms.
