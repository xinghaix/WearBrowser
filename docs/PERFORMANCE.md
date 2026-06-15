# Performance Strategy

Android watches have small batteries, limited memory and slower input. WearBrowser follows a conservative performance model.

## WebView

- Keep one active WebView in the baseline.
- Persist tab URLs and titles instead of keeping many WebViews alive.
- Avoid heavy JavaScript processing unless Reader Mode or Watch Layout Engine is enabled.
- Disable visible scrollbars and overscroll glow for reduced visual noise.

## Tabs

- Cap tabs at 10.
- Mark inactive tabs as Warm, Sleeping or SnapshotOnly based on idle duration.
- Future optimization: snapshot inactive tabs and destroy background WebViews.

## Compose

- Keep overlays small.
- Auto-hide toolbar and zoom overlay.
- Isolate design tokens and safe-area calculations.
- Avoid large recomposition surfaces outside the main browser state.

## Storage

- Use SharedPreferences for lightweight local state in the baseline.
- Defer Room until bookmark/history search, sync or imports justify it.

## Battery

- OLED black default.
- Immersive mode default.
- No telemetry or background sync in the baseline.
