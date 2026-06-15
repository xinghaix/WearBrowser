# ADR-0004: Treat Watch Layout Engine as a first-class product module

Status: Accepted

Date: 2026-06-15

## Context

A normal desktop or phone page is often unusable on a watch, especially on round screens. Simple zoom is not enough.

The product vision requires a Watch Layout Engine that can optimize content for tiny and round displays.

## Decision

Treat Watch Layout Engine (WLE) as a first-class module rather than a small Reader Mode helper.

WLE should eventually handle:

- Content block detection
- Sidebar / footer / ad suppression
- Main content centering
- Round safe-area optimization
- Per-site layout profiles
- Reader Mode integration

## Consequences

Positive:

- Creates a defensible product advantage.
- Keeps page optimization logic out of UI code.
- Enables progressive improvement site by site.

Negative:

- WLE is complex and must remain experimental until validated.
- DOM manipulation and injected scripts require safety and compatibility review.

## Rules

- WLE behavior must be feature-flagged.
- Site-specific rules must be isolated and documented.
- WLE should never break basic browsing when disabled.
