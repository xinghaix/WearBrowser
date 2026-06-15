# Sprint 5: First Runnable Browser Planning

## Goal

Move WearBrowser from foundation implementation to a first runnable Android browser that can be installed and manually tested.

## Non-goals

This sprint does not try to finish all watch-first interactions. It focuses on a reliable basic browsing path first.

## Required user flow

1. Launch the app.
2. See a usable browser surface.
3. Enter a URL or search query.
4. Navigate to the page.
5. Use Back / Forward / Reload.
6. Reset zoom.
7. Handle basic page load error.
8. Exit or return home without crash.

## Implementation tasks

### 1. Address/search input hardening

Acceptance criteria:

- `github.com` becomes `https://github.com`.
- `https://example.com` loads directly.
- `wear browser` becomes a search URL.
- Empty input is ignored.
- Unit tests cover URL, domain, localhost, IP, and search query cases.

### 2. BrowserEngine smoke path

Acceptance criteria:

- `BrowserController.load()` reaches `BrowserEngine.load()`.
- WebView listener updates title, URL, progress, and navigation flags.
- Failed page load emits a visible error state.

### 3. Minimal browser controls

Acceptance criteria:

- Back works when `canGoBack=true`.
- Forward works when `canGoForward=true`.
- Reload calls engine reload.
- Home returns to the configured home URL.

### 4. Manual install validation

Acceptance criteria:

- `./gradlew assembleDebug` succeeds.
- Debug APK installs on emulator or device.
- App launches without crash.
- One HTTPS site loads successfully.

### 5. First manual test checklist

Acceptance criteria:

- Add `docs/05-development/MANUAL_TEST_MATRIX.md`.
- Include square screen, round screen, emulator, and physical device sections.
- Include WebView, input, navigation, zoom, and lifecycle checks.

## Completion criteria

Sprint 5 is complete when:

- Quality gate is green locally and in GitHub Actions.
- The debug APK installs and opens.
- A URL can be entered and loaded.
- Back / Forward / Reload can be manually validated.
- Known limitations are documented.
