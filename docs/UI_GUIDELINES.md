# UI Guidelines

WearBrowser UI is optimized for short, imprecise interactions on very small displays.

## Visual Style

- OLED black as the default background.
- High contrast text.
- Translucent surfaces over web content.
- Capsule inputs and rounded floating controls.
- Minimal chrome: the page should be the default UI.

## Round Screens

- Never assume rectangular corners are visible.
- Keep primary controls in the center band or circular overlays.
- Use Safe Area modes instead of hardcoding one padding value.
- Edge Pie Menu is preferred over dense bottom navigation on round displays.

## Interaction Density

- Prefer gestures for frequent actions.
- Prefer large tap targets when buttons are needed.
- Avoid menus deeper than two levels.
- Hide controls automatically after short inactivity.

## Motion

- Keep animations short and functional.
- Zoom overlay should appear immediately and disappear quickly.
- Dock reveal/hide should not block WebView interaction.

## Copy

- Use short labels: Back, Tabs, Reader, OLED, 100%.
- Avoid long explanatory text except in onboarding and documentation.
