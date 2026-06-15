# Branch Protection

WearBrowser protects the default branch so product and infrastructure changes must pass the quality gate before merging.

## Local setup

```bash
scripts/setup-branch-protection.sh xinghaix/WearBrowser master
```

By default the script requires this check:

```text
Android Quality Gate / assemble
```

If GitHub shows a different check name, pass it explicitly:

```bash
scripts/setup-branch-protection.sh xinghaix/WearBrowser master "Android Quality Gate / assemble"
```

## Why the script uses JSON input

GitHub's branch protection endpoint requires a complete payload for `required_status_checks`, `required_pull_request_reviews`, `restrictions`, and other branch protection options. The script uses a JSON file with `gh api --input` so booleans and null values are sent as real JSON types instead of shell strings.

## Required permissions

The authenticated GitHub user must have admin/owner permission on the repository.
