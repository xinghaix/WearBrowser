#!/usr/bin/env bash
set -euo pipefail

REPO="${1:-}"
BRANCH="${2:-master}"
REQUIRED_CHECK="${3:-Android Quality Gate / assemble}"

if [[ -z "$REPO" ]]; then
  echo "Usage: scripts/setup-branch-protection.sh <owner/repo> [branch] [required-check-name]" >&2
  echo "Example: scripts/setup-branch-protection.sh xinghaix/WearBrowser master" >&2
  exit 1
fi

if ! command -v gh >/dev/null 2>&1; then
  echo "GitHub CLI (gh) is required." >&2
  exit 1
fi

if ! gh auth status >/dev/null 2>&1; then
  echo "gh is not authenticated. Run: gh auth login" >&2
  exit 1
fi

TMP_JSON="$(mktemp)"
trap 'rm -f "$TMP_JSON"' EXIT

# GitHub's branch-protection PUT endpoint expects a complete JSON payload.
# Use --input to avoid gh CLI field typing issues where booleans/nulls can become strings.
cat > "$TMP_JSON" <<JSON
{
  "required_status_checks": {
    "strict": true,
    "contexts": ["$REQUIRED_CHECK"]
  },
  "enforce_admins": true,
  "required_pull_request_reviews": {
    "dismiss_stale_reviews": true,
    "require_code_owner_reviews": false,
    "required_approving_review_count": 1,
    "require_last_push_approval": false
  },
  "restrictions": null,
  "required_linear_history": false,
  "allow_force_pushes": false,
  "allow_deletions": false,
  "block_creations": false,
  "required_conversation_resolution": true,
  "lock_branch": false,
  "allow_fork_syncing": true
}
JSON

echo "Configuring branch protection for $REPO:$BRANCH"
echo "Required status check: $REQUIRED_CHECK"

gh api \
  --method PUT \
  -H "Accept: application/vnd.github+json" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  "/repos/$REPO/branches/$BRANCH/protection" \
  --input "$TMP_JSON"

echo "Branch protection configured."
