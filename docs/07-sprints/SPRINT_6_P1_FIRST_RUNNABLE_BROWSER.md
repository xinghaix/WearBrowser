# Sprint 6: P1 First Runnable Browser

## Goal

P1 focuses on the smallest real browser loop:

- launch app
- display WebView
- enter a URL or search query
- navigate to the target page
- go back / forward
- reload / stop
- return home
- adjust zoom
- verify the app can be built and manually tested

This sprint intentionally avoids expanding experimental watch-first features. Edge gestures, Pie Menu hardening, WLE and full download management remain later milestones.

## Acceptance Criteria

- `./scripts/quality-gate.sh` passes locally.
- `./gradlew assembleDebug` produces a debug APK.
- App launches without crash.
- Address bar can open:
  - `https://example.com`
  - `github.com`
  - `wear browser`
- Android back button navigates WebView history before exiting the app.
- Toolbar Back / Forward / Reload / Home work.
- Zoom controls update the WebView between 50% and 300%.
- Basic page title, loading progress and error messages appear in UI.

## Manual Test Checklist

1. Install debug APK on emulator or watch.
2. Launch WearBrowser.
3. Confirm Google home loads.
4. Enter `example.com`, tap Go.
5. Enter `wear browser`, tap Go.
6. Tap Back and confirm previous page returns.
7. Tap Forward and confirm next page returns.
8. Tap Reload.
9. Tap `+`, `-`, and `100%` zoom controls.
10. Double tap page area and confirm zoom resets to 100%.

## Notes

This sprint replaces the prototype browser surface with a simpler, more reliable first-runnable UI. It keeps the BrowserEngine abstraction in place so later watch-first interaction work can be layered on top without UI-to-WebView coupling.
