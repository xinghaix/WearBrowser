# WearBrowser

[![Android Quality Gate](https://github.com/xinghaix/WearBrowser/actions/workflows/android.yml/badge.svg)](https://github.com/xinghaix/WearBrowser/actions/workflows/android.yml)
[![License: Apache-2.0](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/platform-Wear%20OS%20%2F%20Android-green.svg)](#)
[![Phase](https://img.shields.io/badge/phase-implementation%20sprint-orange.svg)](docs/07-sprints/IMPLEMENTATION_STATUS.md)

WearBrowser is an open-source browser designed for Android watches and small-screen Android devices.

It is **not** a phone browser squeezed into a watch. It is designed around watch-first interactions: global edge back, one-finger zoom, round-screen safe area, gesture-first navigation, per-site layout memory, immersive browsing, and a watch-oriented Reader Mode.

## Current implementation phase

WearBrowser is currently in **Phase 3: Implementation Sprint 4**.

The product and architecture documentation is mature, and the source code now contains concrete implementations of the core abstractions plus the first hardened watch-first gesture pipeline and lifecycle-safe WebView ownership:

- `BrowserEngine`
- `BrowserController`
- `GestureEngine`
- `GestureDispatcher`
- `ZoomPolicy`
- `TabPolicy`
- `WatchLayoutEngine`
- Repository interfaces
- Design tokens

See [`docs/07-sprints/IMPLEMENTATION_STATUS.md`](docs/07-sprints/IMPLEMENTATION_STATUS.md) for the latest engineering status.

## Current status

`v1.3.3-foundation-sprint-4` is an implementation sprint build. It contains the browser core, watch-first interaction layer, product-quality scaffolding, CI configuration, documentation, and release preparation files.

Android SDK validation has started, and the project is being hardened through a quality gate before feature expansion.

## Features

- Android WebView browser
- Square and round screen support
- Global left-edge back gesture
- Right-edge forward gesture
- Right-edge one-finger vertical zoom
- 50%~300% smooth zoom model
- Double tap to reset zoom to 100%
- Top/bottom edge gestures to reveal controls
- Long-press Edge Pie Menu
- Fit-width shortcut
- Per-site profile memory: zoom, UA, OLED, reader, and safe-area mode
- Auto site profiles for GitHub, Wikipedia, and local/router pages
- Auto / Strict / Fullscreen / Reader Optimized safe-area modes
- Reader Mode prototype for article-like pages
- Immersive full-screen browsing with auto-hiding dock
- First-run gesture onboarding
- Bookmarks
- History
- Lightweight tab manager, capped at 10 tabs
- Tab lifecycle states: Active, Warm, Sleeping, SnapshotOnly
- Desktop/mobile UA switch
- OLED black mode
- Clear WebView cache and local history
- Feature flags for experimental modules
- Product design tokens and OLED-first theme
- Lifecycle-safe WebView pause/resume/destroy path
- CI quality gate, Detekt config, and unit-test scaffolding

## Interaction model

| Gesture | Action |
|---|---|
| Tap | Show / hide toolbar |
| Long press | Open Edge Pie Menu |
| Double tap | Reset zoom to 100% |
| Left edge swipe | Back; home if no WebView history |
| Right edge horizontal swipe | Forward |
| Right edge vertical drag | Zoom 50%~300% |
| Bottom edge swipe up | Reveal dock |
| Top edge swipe down | Reveal address/search bar dock |

## Product principles

1. **Watch-first navigation**: gestures are primary, tiny buttons are secondary.
2. **Round-screen correctness**: round displays are a first-class target.
3. **Best-in-class zoom**: 50%~300%, one-finger edge zoom, double-tap reset, and per-site memory.
4. **Page-first UI**: immersive by default, controls appear only when needed.
5. **Local-first privacy**: no telemetry, no analytics SDK, and local profile storage.
6. **Engineering discipline**: feature flags, quality gate, static analysis, tests, and release checklists.

## Build

```bash
./gradlew assembleDebug
```

## Quality gate

Use the local quality gate before pushing:

```bash
./scripts/quality-gate.sh
```

Equivalent Gradle commands:

```bash
./gradlew detekt
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

## Project layout

```text
app/src/main/java/com/wearbrowser/
├── MainActivity.kt
├── browser/       # state, persistence, URL normalization, UI
├── config/        # product constants and feature flags
├── design/        # design tokens and theme
├── engine/        # BrowserEngine and WebView implementation
├── gesture/       # gesture engine and actions
├── performance/   # tab sleep and overlay timing policy
├── product/       # onboarding, product copy, release readiness
├── ui/            # round/square safe-area calculation
└── webview/       # WebView wrapper and compatibility shims
```

## Documentation

- [`docs/01-product/PRODUCT.md`](docs/01-product/PRODUCT.md)
- [`docs/02-architecture/ARCHITECTURE.md`](docs/02-architecture/ARCHITECTURE.md)
- [`docs/03-ui/DESIGN_SYSTEM.md`](docs/03-ui/DESIGN_SYSTEM.md)
- [`docs/05-development/GITHUB_GOVERNANCE.md`](docs/05-development/GITHUB_GOVERNANCE.md)
- [`docs/05-development/PROJECT_MANAGEMENT.md`](docs/05-development/PROJECT_MANAGEMENT.md)
- [`PRIVACY.md`](PRIVACY.md)
- [`ROADMAP.md`](ROADMAP.md)

## Governance

WearBrowser uses a product-grade open-source workflow:

- Pull requests must pass the quality gate.
- Issues use structured templates for bugs, build failures, feature requests, and UX feedback.
- Architecture-level changes should be documented in ADRs.
- Experimental behavior should be protected by feature flags.
- Release notes are drafted from merged pull requests.

See [`docs/05-development/GITHUB_GOVERNANCE.md`](docs/05-development/GITHUB_GOVERNANCE.md) for details.

## Release notes

See [`CHANGELOG.md`](CHANGELOG.md).

## License

Apache-2.0
