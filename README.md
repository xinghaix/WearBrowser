# WearBrowser

## Current implementation phase

WearBrowser is currently in **Phase 3: Implementation Sprint 4**. The product and architecture documentation is mature, and the source code now contains concrete implementations of the core abstractions plus the first hardened watch-first gesture pipeline plus lifecycle-safe WebView ownership: `BrowserEngine`, `BrowserController`, `GestureEngine`, `GestureDispatcher`, `ZoomPolicy`, `TabPolicy`, `WatchLayoutEngine`, repositories, and design tokens.

See `docs/07-sprints/IMPLEMENTATION_STATUS.md` for the latest engineering status.


WearBrowser is an open-source browser designed for Android watches and small-screen Android devices.

It is not a phone browser squeezed into a watch. It is designed around watch-first interactions: global edge back, one-finger zoom, round-screen safe area, gesture-first navigation, per-site layout memory, immersive browsing and a watch-oriented Reader Mode.

## Current Status

`v1.3.3-foundation-sprint-4` is an implementation sprint build. It contains the browser core, watch-first interaction layer, product-quality scaffolding, CI configuration, documentation and release preparation files. Android SDK validation still needs to be run in a real Android development environment.

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
- Per-site profile memory: zoom, UA, OLED, reader and safe-area mode
- Auto site profiles for GitHub, Wikipedia and local/router pages
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
- CI quality gate, Detekt config and unit-test scaffolding

## Interaction Model

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

## Product Principles

1. **Watch-first navigation**: gestures are primary, tiny buttons are secondary.
2. **Round-screen correctness**: round displays are a first-class target.
3. **Best-in-class zoom**: 50%~300%, one-finger edge zoom, double-tap reset and per-site memory.
4. **Page-first UI**: immersive by default, controls appear only when needed.
5. **Local-first privacy**: no telemetry, no analytics SDK and local profile storage.
6. **Engineering discipline**: feature flags, quality gate, static analysis, tests and release checklists.

## Build

```bash
./gradlew assembleDebug
```

If the Gradle wrapper is not present in your checkout, use Android Studio or a local Gradle installation to generate it once:

```bash
gradle wrapper
./gradlew assembleDebug
```

## Quality Gate

```bash
./gradlew detekt testDebugUnitTest assembleDebug
```

## Project Layout

```text
app/src/main/java/com/wearbrowser/
├── MainActivity.kt
├── browser/       # state, persistence, URL normalization, UI
├── config/        # product constants and feature flags
├── design/        # design tokens and theme
├── gesture/       # gesture constants
├── performance/   # tab sleep and overlay timing policy
├── product/       # onboarding, product copy, release readiness
├── ui/            # round/square safe-area calculation
└── webview/       # WebView wrapper and settings
```

## Documentation

- `docs/ARCHITECTURE.md`
- `docs/PRODUCT_QUALITY.md`
- `docs/RELEASE.md`
- `PRIVACY.md`
- `ROADMAP.md`

## Release Notes

See `CHANGELOG.md`.

## License

Apache-2.0


## Current Implementation Sprint

Sprint 2 integrates the Compose browser surface with `BrowserEngine + BrowserController`, reducing direct WebView coupling and preparing the project for real build validation.
