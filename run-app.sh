#!/usr/bin/env bash

set -euo pipefail

# Usage: ./scripts/run-app.sh
# The script will source `./.env` if it exists and then run the jar with those env vars.

if [[ -f .env ]]; then
  set -a
  . ./.env
  set +a
fi

JAR=target/policeforcedogs-0.1.jar
if [[ ! -f "${JAR}" ]]; then
  echo "Jar not found (${JAR}). Build first with from PoliceForceDogs folder: mvn package" >&2
  exit 1
fi

echo "Starting app with JDBC_URL=${JDBC_URL:-<default in application.properties>} JDBC_USER=${JDBC_USER:-sa}"

ENV_CMD=(env)
if [[ -n "${JDBC_URL:-}" ]]; then
  ENV_CMD+=(JDBC_URL="${JDBC_URL}")
fi
if [[ -n "${JDBC_USER:-}" ]]; then
  ENV_CMD+=(JDBC_USER="${JDBC_USER}")
fi

ENV_CMD+=(JDBC_PASSWORD="${JDBC_PASSWORD:-}")

ENV_CMD+=(java -jar "${JAR}")

exec "${ENV_CMD[@]}"


