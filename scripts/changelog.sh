#!/usr/bin/env bash
# Print the raw changelog between two release tags.
# Usage: scripts/changelog.sh <prev-tag> [new-tag]
#   scripts/changelog.sh v1.5.10 v1.5.11
#   scripts/changelog.sh v1.5.10        # new-tag defaults to HEAD
set -euo pipefail

if [ $# -lt 1 ]; then
    echo "usage: $0 <prev-tag> [new-tag]" >&2
    exit 2
fi

prev="$1"
new="${2:-HEAD}"

if ! git rev-parse -q --verify "refs/tags/${prev}" >/dev/null; then
    echo "error: tag '${prev}' not found" >&2
    exit 1
fi

repo_url="$(git config --get remote.origin.url | sed -E 's#(git@|https://)github.com[:/]#https://github.com/#; s#\.git$##')"

echo "## Changes since ${prev}"
echo
echo "### Commits"
git log --pretty=format:'- %s (`%h`)' "${prev}..${new}"
echo
echo
echo "### Files"
git diff --stat "${prev}..${new}"
echo
echo
echo "### Compare"
echo "${repo_url}/compare/${prev}...${new}"
