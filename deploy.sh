#!/bin/bash

# Variables
PROJECT_ID="marsupilamieue"
IMAGE_NAME="be-payment-deploy"
VERSION=$1
CONTAINER_NAME="be-payment"
VERSION_FILE="current_version.txt"
UPDATE_VERSION_FILE=$2

# Function to deploy a specific version
deploy_version() {
  echo "Deploying version $VERSION..."

  # Check if a container with the same name is already running or exists
  if [ "$(sudo docker ps -a -q -f name=$CONTAINER_NAME)" ]; then
    echo "Stopping and removing existing container..."
    sudo docker stop $CONTAINER_NAME
    sudo docker rm $CONTAINER_NAME
  fi

  # Pull the new image version
  sudo docker pull $PROJECT_ID/$IMAGE_NAME:$VERSION

  # Run the new container
  sudo docker run -d -p 80:8080 --name $CONTAINER_NAME $PROJECT_ID/$IMAGE_NAME:$VERSION

  # Save the current version to a file if the second argument is "latest"
  if [ "$UPDATE_VERSION_FILE" = "latest" ]; then
    echo "Saving current version to file..."
    echo $VERSION > $VERSION_FILE
  fi
}

# Main script logic
if [ -z "$VERSION" ]; then
  echo "Please provide the version to deploy. Usage: ./deploy.sh <version> [latest]"
  exit 1
fi

deploy_version