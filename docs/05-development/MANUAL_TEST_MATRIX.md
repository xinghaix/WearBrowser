# Manual Test Matrix

This checklist is used before alpha releases and after major interaction changes.

## Environment

| Target | Required | Notes |
|---|---:|---|
| Android emulator | Yes | First build validation |
| Wear OS round emulator | Yes | Round safe-area validation |
| Wear OS square/rectangular target | Recommended | Layout validation |
| Physical watch | Recommended | Gesture and performance validation |
| Android phone/small device | Optional | Compatibility validation |

## Smoke test

- [ ] App installs.
- [ ] App launches.
- [ ] No crash on first launch.
- [ ] Home page loads or shows a clear fallback.
- [ ] Debug build can be uninstalled cleanly.

## Address/search

- [ ] Full HTTPS URL loads.
- [ ] Plain domain loads.
- [ ] Search query opens search engine.
- [ ] Empty input is ignored.
- [ ] Clipboard URL can be pasted through system input.

## Navigation

- [ ] Back works after navigating to a second page.
- [ ] Forward works after going back.
- [ ] Reload works.
- [ ] Home works.
- [ ] No-history back behavior is safe.

## Zoom

- [ ] 100% reset works.
- [ ] Zoom clamps to 50% minimum.
- [ ] Zoom clamps to 300% maximum.
- [ ] Fit-width action does not crash.

## Gestures

- [ ] Left-edge back is not triggered by normal vertical scroll.
- [ ] Right-edge zoom is not triggered by normal page scroll.
- [ ] Long press opens Edge Pie Menu.
- [ ] Top/bottom edge reveal controls.

## Round screen

- [ ] Main content is not clipped by round corners.
- [ ] Dock remains reachable.
- [ ] Address/search controls remain readable.
- [ ] Reader mode keeps text centered.

## Lifecycle

- [ ] Home button / app background does not crash.
- [ ] Resume restores current page.
- [ ] Rotate / configuration change does not crash where applicable.
- [ ] WebView releases when app is closed.

## Downloads

- [ ] Download callback is triggered for a downloadable file.
- [ ] Permission behavior is clear.
- [ ] Failure is visible to the user.

## Known limitations

Document every failed checkbox before release.
