# ADR-0003: Use a GestureEngine for watch-first interaction

Status: Accepted

Date: 2026-06-15

## Context

WearBrowser's core differentiation is watch-first interaction: edge back, edge forward, edge zoom, Pie Menu, immersive controls, and round-screen ergonomics.

Embedding gesture logic directly in Compose screens would make behavior difficult to tune and test.

## Decision

Use a dedicated `GestureEngine` and `GestureDispatcher` to classify pointer movement into semantic actions.

Examples:

- Edge Back
- Edge Forward
- Edge Zoom
- Reveal Dock
- Reveal Address Bar
- Open Pie Menu
- Reset Zoom

## Consequences

Positive:

- Gesture behavior is testable without UI rendering.
- Tuning thresholds is centralized.
- Watch-first interactions can evolve independently from UI components.
- Conflicts between gestures can be reasoned about explicitly.

Negative:

- Requires careful configuration to avoid overengineering.
- Needs real-device tuning because watch screens differ widely.

## Rules

- Gesture thresholds must be configurable.
- Any new global gesture must have tests.
- Basic browsing must remain usable without advanced gestures.
