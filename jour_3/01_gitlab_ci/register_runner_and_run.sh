#!/bin/bash

set -e

GITLAB_URL="${GITLAB_URL:-http://gitlab}"
GITLAB_PERSONAL_ACCESS_TOKEN="${GITLAB_PERSONAL_ACCESS_TOKEN}"

# wait to ensure GitLab API is responsive
until curl --fail "${GITLAB_URL}/-/health" > /dev/null 2>&1; do
  echo "Waiting for GitLab health check..."
  sleep 2
done

echo "GitLab is ready!"

# Check if runner is already registered
if [ -f /etc/gitlab-runner/config.toml ] && grep -q "token" /etc/gitlab-runner/config.toml; then
  echo "Runner already registered, skipping registration..."
  exec gitlab-runner run
fi

if [ -z "$GITLAB_PERSONAL_ACCESS_TOKEN" ]; then
  echo "ERROR: GITLAB_PERSONAL_ACCESS_TOKEN environment variable is not set"
  echo ""
  echo "Please create a Personal Access Token in GitLab:"
  echo "1. Login to GitLab at ${GITLAB_URL}"
  echo "2. Go to User Settings > Access Tokens"
  echo "3. Create a token with 'api' scope"
  echo "4. Set it as GITLAB_PERSONAL_ACCESS_TOKEN environment variable"
  exit 1
fi

echo "Using provided Personal Access Token..."
PRIVATE_TOKEN="$GITLAB_PERSONAL_ACCESS_TOKEN"

echo "Creating new runner via API..."

RUNNER_RESPONSE=$(curl -s --request POST "${GITLAB_URL}/api/v4/user/runners" \
  --header "PRIVATE-TOKEN: ${PRIVATE_TOKEN}" \
  --header "Content-Type: application/json" \
  --data '{
    "runner_type": "instance_type",
    "description": "shell-runner",
    "tag_list": ["shell", "linux"],
    "run_untagged": true,
    "locked": false,
    "access_level": "not_protected"
  }')

RUNNER_TOKEN=$(echo "$RUNNER_RESPONSE" | grep -o '"token":"glrt-[^"]*"' | cut -d'"' -f4)

if [ -z "$RUNNER_TOKEN" ]; then
  echo "ERROR: Could not fetch runner token"
  exit 1
fi

echo "Runner token obtained: ${RUNNER_TOKEN:0:10}..."

echo "Registering GitLab Runner..."

mkdir -p /etc/gitlab-runner

# Install dependencies for shell executor
apt-get update
apt-get install -y make docker.io curl ca-certificates

# Install Docker buildx plugin
BUILDX_VERSION="v0.12.1"
mkdir -p /usr/local/lib/docker/cli-plugins
curl -SL "https://github.com/docker/buildx/releases/download/${BUILDX_VERSION}/buildx-${BUILDX_VERSION}.linux-amd64" \
  -o /usr/local/lib/docker/cli-plugins/docker-buildx
chmod +x /usr/local/lib/docker/cli-plugins/docker-buildx

# Verify buildx installation
docker buildx version

cat > /etc/gitlab-runner/config.toml <<EOF
concurrent = 1
check_interval = 0

[session_server]
session_timeout = 1800

[[runners]]
name = "shell-runner"
url = "${GITLAB_URL}"
token = "${RUNNER_TOKEN}"
executor = "shell"
[runners.custom_build_dir]
[runners.cache]
  [runners.cache.s3]
  [runners.cache.gcs]
  [runners.cache.azure]
EOF

echo "Runner registration complete!"

# Start the runner
exec gitlab-runner run
