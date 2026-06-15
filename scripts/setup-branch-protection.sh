#!/usr/bin/env bash
set -euo pipefail

REPO="${1:-xinghaix/WearBrowser}"
BRANCH="${2:-master}"

if ! command -v gh >/dev/null 2>&1; then
  echo "GitHub CLI is required: https://cli.github.com/"
  exit 1
fi

gh api \
  --method PUT \
  -H "Accept: application/vnd.github+json" \
  "/repos/$REPO/branches/$BRANCH/protection" \
  -f required_status_checks.strict=true \
  -F required_status_checks.contexts[]='Build, test and static analysis' \
  -f enforce_admins=false \
  -F required_pull_request_reviews.required_approving_review_count=0 \
  -f restrictions=null \
  -f allow_force_pushes=false \
  -f allow_deletions=false

echo "Branch protection configured for $REPO:$BRANCH"
