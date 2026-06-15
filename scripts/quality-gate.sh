#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

if [[ ! -x ./gradlew ]]; then
  chmod +x ./gradlew
fi

./gradlew --no-daemon detekt
./gradlew --no-daemon testDebugUnitTest
./gradlew --no-daemon assembleDebug
