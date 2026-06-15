# Contributing

Thanks for helping improve WearBrowser.

Good first areas:

- Gesture polish on real watches
- Round-screen layout testing
- WebView settings
- Reader mode
- Download manager
- UI copy and translations
- Documentation

Before submitting a PR:

1. Keep the app lightweight.
2. Avoid large dependencies unless necessary.
3. Test on both square and round screens when possible.
4. Prefer simple, readable Kotlin code.
5. Document user-visible gesture changes in README.

## Local Build

```bash
gradle assembleDebug
```

or, if your checkout has a Gradle wrapper:

```bash
./gradlew assembleDebug
```
