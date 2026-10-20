#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
zip_path="$repo_root/lava-core-1.0.7-BETA-src.zip"

if [ ! -f "$zip_path" ]; then
  echo "Source zip not found: $zip_path" >&2
  exit 1
fi

if [ -d "$repo_root/src" ]; then
  rm -rf "$repo_root/src"
fi

tmpdir="$(mktemp -d)"
trap 'rm -rf "$tmpdir"' EXIT

unzip -oq "$zip_path" -d "$tmpdir"

extracted_src=""

if [ -d "$tmpdir/src" ]; then
  extracted_src="$tmpdir/src"
else
  extracted_src="$(find "$tmpdir" -type d -path '*/src' | head -n 1 || true)"
fi

if [ -z "$extracted_src" ] || [ ! -d "$extracted_src" ]; then
  echo "The zip archive does not contain a src directory." >&2
  exit 1
fi

cp -a "$extracted_src" "$repo_root/src"

echo "Updated $repo_root/src from $zip_path"
