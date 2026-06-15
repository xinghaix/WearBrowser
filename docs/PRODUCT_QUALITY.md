# Product Quality Plan

WearBrowser is treated as an engineering product, not a demo. Quality is organized into five tracks.

## P0 Engineering Quality

- Layered package structure: browser, webview, gesture, ui, design, config, product, performance.
- Feature flags for experimental capabilities.
- Detekt configuration for static analysis.
- GitHub Actions quality gate for detekt, unit tests and debug build.
- Unit tests for URL normalization and zoom contract.
- Product constants centralized in `AppConfig` and `PerformancePolicy`.

## P1 Performance

- Maximum 10 tabs.
- Tab lifecycle policy: Warm, Sleeping and SnapshotOnly.
- Auto-hide toolbar and zoom overlay to reduce recomposition and OLED burn-in risk.
- Safe-area calculation isolated to screen configuration inputs.
- Watch Layout Engine is CSS/JS injection only; no heavy parser dependency.
- Local SharedPreferences persistence avoids database startup overhead for the baseline.

## P2 UI Polish

- Compose Material 3 dark theme.
- Centralized colors, spacing and shapes in `WearTheme`.
- OLED-first visual language.
- Immersive page-first layout.
- Edge hints, translucent overlays, capsule controls and circular Pie Menu.

## P3 Product Experience

- First-run onboarding overlay for core gestures.
- Local-first privacy policy.
- Round and square screen product positioning.
- Clear interaction model in README.
- Error, release and testing documentation.

## P4 Release Standard

- GitHub issue templates.
- Release checklist.
- F-Droid metadata baseline.
- Changelog and privacy policy.
- CI quality gate definition.

## Remaining validation

The current environment cannot run the Android SDK. Before tagging a public release, validate on:

1. Round Wear OS emulator/device.
2. Square Android small-screen emulator/device.
3. Real Android System WebView variants.
4. Low-memory tab behavior.
5. Gesture conflict cases on scroll-heavy pages.
