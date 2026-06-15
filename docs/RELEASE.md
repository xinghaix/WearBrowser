# Release Checklist

1. Update `versionCode` and `versionName` in `app/build.gradle.kts`.
2. Update `CHANGELOG.md`.
3. Build debug APK: `./gradlew assembleDebug`.
4. Build release APK/AAB after configuring signing.
5. Test at least one square and one round Wear OS emulator/device.
6. Test gestures: left back, right forward, right vertical zoom, double tap reset.
7. Test bookmarks, history, tabs, desktop UA and cache cleanup.
8. Attach APK and changelog to GitHub Release.
