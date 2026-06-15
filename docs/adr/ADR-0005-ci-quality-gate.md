# ADR-0005: Enforce a CI quality gate on every push and pull request

Status: Accepted

Date: 2026-06-15

## Context

WearBrowser is an open-source product. As contributors join, the project needs a reliable baseline that prevents broken code from entering the main branch.

## Decision

Run an Android CI quality gate on every push and pull request.

Required checks:

- Static analysis: `./gradlew detekt`
- Unit tests: `./gradlew testDebugUnitTest`
- Debug build: `./gradlew assembleDebug`

CI should also upload test reports and debug APK artifacts.

## Consequences

Positive:

- Keeps the main branch buildable.
- Gives contributors immediate feedback.
- Makes releases safer.
- Turns “可编译主干” into an enforceable rule.

Negative:

- CI time increases for every PR.
- Some early experimental work may require feature branches.

## Follow-up

Once the project grows, add:

- Android lint
- Screenshot tests
- Macrobenchmark
- Release signing workflow
- Dependency review
