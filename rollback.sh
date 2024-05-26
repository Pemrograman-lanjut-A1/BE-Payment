#!/bin/bash

# Variables
PROJECT_ID="marsupilamieue"
IMAGE_NAME="be-payment-deploy"
CONTAINER_NAME="be-payment"
VERSION_FILE="current_version.txt"

# Function to rollback to the previous version
rollback() {
  echo "Rolling back to the previous version..."
  PREVIOUS_VERSION=$(sudo docker images --format "{{.Tag}}" $PROJECT_ID/$IMAGE_NAME | sort -r | sed -n '2p')
  if [ -z "$PREVIOUS_VERSION" ]; then
    echo "No previous version found to rollback to."
    exit 1
  fi

  # Deploy the previous version
  ./deploy.sh $PREVIOUS_VERSION
}

# Function to rollback to the latest version
rollback_to_latest() {
  if [ -f $VERSION_FILE ]; then
    LATEST_VERSION=$(cat $VERSION_FILE)
    echo "Rolling back to the latest version $LATEST_VERSION..."
    ./deploy.sh $LATEST_VERSION
  else
    echo "No latest version found. Please deploy a version first."
    exit 1
  fi
}

# Main script logic
if [ "$1" == "latest" ]; then
  rollback_to_latest
else
  rollback
fi