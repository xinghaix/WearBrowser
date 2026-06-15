#!/usr/bin/env bash
set -euo pipefail

# WearBrowser local quality gate.
# Prefer stable JetBrains/Android Studio JBRs before falling back to system Java.
# This avoids running Detekt/Kotlin analysis on unsupported bleeding-edge JDKs.

candidate_jdks=(
  "$HOME/Applications/IntelliJ IDEA.app/Contents/jbr/Contents/Home"
  "/Applications/IntelliJ IDEA.app/Contents/jbr/Contents/Home"
  "/Applications/Android Studio.app/Contents/jbr/Contents/Home"
)

for jdk in "${candidate_jdks[@]}"; do
  if [ -x "$jdk/bin/java" ]; then
    version_output="$($jdk/bin/java --version 2>&1 | head -n 1 || true)"
    if [[ "$version_output" =~ (17|21|22) ]]; then
      export JAVA_HOME="$jdk"
      export PATH="$JAVA_HOME/bin:$PATH"
      break
    fi
  fi
done

if [ -z "${JAVA_HOME:-}" ] && command -v /usr/libexec/java_home >/dev/null 2>&1; then
  if /usr/libexec/java_home -v 17 >/dev/null 2>&1; then
    export JAVA_HOME="$(/usr/libexec/java_home -v 17)"
    export PATH="$JAVA_HOME/bin:$PATH"
  elif /usr/libexec/java_home -v 21 >/dev/null 2>&1; then
    export JAVA_HOME="$(/usr/libexec/java_home -v 21)"
    export PATH="$JAVA_HOME/bin:$PATH"
  fi
fi

echo "Using Java:"
java --version

./gradlew --no-daemon detekt
./gradlew --no-daemon testDebugUnitTest
./gradlew --no-daemon assembleDebug
