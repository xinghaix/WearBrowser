# Architecture

WearBrowser is intentionally lightweight. The codebase prefers simple Kotlin and Android platform APIs over large dependencies.

## Layers

- `browser`: Compose UI, browser state, URL normalization, local persistence and product behavior.
- `webview`: Android WebView creation, settings, page callbacks, downloads and progress updates.
- `gesture`: gesture constants used by the full-screen interaction layer.
- `ui`: round/square safe-area calculation.
- `design`: Material 3 theme wrapper, OLED-first colors, spacing and shape tokens.
- `config`: feature flags and product constants.
- `performance`: tab lifecycle, overlay timing and low-memory policy constants.
- `product`: onboarding, release readiness and product copy.

## State Model

`BrowserState` owns the active URL, title, zoom, tab list, bookmarks, history, site profile flags and WebView reference. UI actions call methods on this state object instead of manipulating WebView directly.

The current implementation intentionally avoids introducing ViewModel, Room or dependency injection until real-device behavior stabilizes. Once the product interaction model is proven, `BrowserState` can be split into controller/repository/view-model classes.

## Persistence

v1.2 uses `SharedPreferences` plus JSON arrays to keep startup overhead low. It persists:

- Bookmarks
- History
- Tabs
- Global settings
- Onboarding completion
- Per-site profiles: zoom, user agent, OLED, reader and safe-area mode

Room can be introduced later if the project needs indexing, sync or heavy bookmark/history operations.

## Feature Flags

`FeatureFlags` provides a stable place to gate experimental modules such as Watch Layout Engine, Edge Pie Menu, haptics, crown input and long screenshots. This keeps the release channel conservative while preserving experimental code paths.

## Watch Layout Engine

The Watch Layout Engine is a pragmatic WebView layer, not a full browser engine. It combines:

1. Safe-area padding for round screens.
2. Zoom applied through `setInitialScale()` and CSS zoom.
3. Per-site profile memory.
4. Reader CSS injection for article-like layouts.
5. OLED black page background adjustments.
6. UA switching for desktop-oriented sites.

The goal is “open and readable” on a watch, not pixel-perfect desktop rendering.

## Tab Lifecycle

Tabs have lifecycle states:

- `Active`: currently loaded tab.
- `Warm`: recent inactive tab.
- `Sleeping`: older inactive tab.
- `SnapshotOnly`: very old inactive tab; intended for future screenshot restore.

The current version records lifecycle state. Future versions can use it to destroy background WebViews and restore from snapshots.

## Zoom

Zoom is represented as a float from `0.5f` to `3.0f`. The app applies both `setInitialScale()` and CSS zoom as a pragmatic WebView baseline. Real devices should be tested because WebView zoom behavior varies by Android WebView implementation.

## Round Screens

Safe-area modes are computed from the shortest screen dimension. The goal is to avoid clipping important content on round displays while still allowing a fullscreen escape hatch.
