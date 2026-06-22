#!/usr/bin/env bash
# Minimal init-db script: runs schema.sql then data.sql using the bundled H2 jar.
set -e

# Run from project root:
#   ./scripts/init-db.sh

H2_JAR="lib/h2-2.3.232.jar"

if [[ ! -f "${H2_JAR}" ]]; then
  echo "H2 jar ${H2_JAR} not found. Please download it to the lib/ directory or run the one-off command using your preferred H2 jar." >&2
  exit 1
fi

# Run both scripts in a single Shell session so the seed sees the created tables
java -cp "${H2_JAR}" org.h2.tools.Shell \
  -url "jdbc:h2:file:./target/police-force-dogs;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE" \
  -user sa \
  -sql "RUNSCRIPT FROM 'src/main/resources/schema.sql'; RUNSCRIPT FROM 'src/main/resources/data.sql';"

echo "Initialized H2 DB at ./target/police-force-dogs (user: sa, empty password)"
