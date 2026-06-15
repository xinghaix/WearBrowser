#!/usr/bin/env bash
set -euo pipefail

version="${1:-}"
if [ -z "$version" ]; then
  echo "Usage: scripts/create-release-candidate.sh v0.1.0-alpha.1"
  exit 1
fi

./scripts/quality-gate.sh

git diff --quiet || {
  echo "Working tree is not clean. Commit or stash changes first."
  exit 1
}

git tag -a "$version" -m "WearBrowser $version"
git push origin "$version"

echo "Created and pushed tag $version. GitHub Actions will build the release candidate."
