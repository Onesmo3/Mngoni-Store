#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
GRADLE_BIN="/data/data/com.termux/files/home/.gradle/wrapper/dists/gradle-9.9.1-bin/dol8b552jwt4yhd7ihm3iahwb/gradle-9.9.1/bin/gradle"

if [ ! -x "$GRADLE_BIN" ]; then
    echo "Gradle 9.9.1 haipatikani: $GRADLE_BIN" >&2
    exit 1
fi

exec "$GRADLE_BIN" -p "$ROOT" "$@"
