#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
"$ROOT/gradle-local.sh" :core:test :payments:test --console=plain
