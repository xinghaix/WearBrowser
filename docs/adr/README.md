# Architecture Decision Records

This directory records important architectural decisions for WearBrowser.

Rules:

1. Every major architectural change requires an ADR.
2. ADRs are append-only. Do not rewrite history; supersede old ADRs with new ones.
3. ADRs should explain context, decision, consequences, and alternatives.

Status values:

- Proposed
- Accepted
- Superseded
- Deprecated

Index:

- [ADR-0001: Use Android WebView as the initial browser engine](ADR-0001-android-webview-engine.md)
- [ADR-0002: Route browser operations through BrowserEngine and BrowserController](ADR-0002-browser-engine-controller.md)
- [ADR-0003: Use a GestureEngine for watch-first interaction](ADR-0003-gesture-engine.md)
- [ADR-0004: Treat Watch Layout Engine as a first-class product module](ADR-0004-watch-layout-engine.md)
- [ADR-0005: Enforce a CI quality gate on every push and pull request](ADR-0005-ci-quality-gate.md)
