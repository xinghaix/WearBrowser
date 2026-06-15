#!/usr/bin/env bash
set -euo pipefail

BREW_OPENJDK_17="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
BREW_OPENJDK_17_INTEL="/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
INTELLIJ_JBR="$HOME/Applications/IntelliJ IDEA.app/Contents/jbr/Contents/Home"
ANDROID_STUDIO_JBR="/Applications/Android Studio.app/Contents/jbr/Contents/Home"

if [ -d "$BREW_OPENJDK_17" ]; then
  export JAVA_HOME="$BREW_OPENJDK_17"
elif [ -d "$BREW_OPENJDK_17_INTEL" ]; then
  export JAVA_HOME="$BREW_OPENJDK_17_INTEL"
elif [ -d "$INTELLIJ_JBR" ]; then
  export JAVA_HOME="$INTELLIJ_JBR"
elif [ -d "$ANDROID_STUDIO_JBR" ]; then
  export JAVA_HOME="$ANDROID_STUDIO_JBR"
elif command -v /usr/libexec/java_home >/dev/null 2>&1 && /usr/libexec/java_home -v 17 >/dev/null 2>&1; then
  export JAVA_HOME="$(/usr/libexec/java_home -v 17)"
fi

export PATH="$JAVA_HOME/bin:$PATH"

echo "Using Java:"
java --version

echo "Running Detekt report only..."
./gradlew --no-daemon detekt || echo "Detekt reported issues. Continue for P1."

echo "Running unit tests..."
./gradlew --no-daemon testDebugUnitTest

echo "Building debug APK..."
./gradlew --no-daemon assembleDebug