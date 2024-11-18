#!/bin/bash

# Set project directories
PROJECT_DIR=$(git rev-parse --show-toplevel)

# Check if the directory is a Git repository
if [ $? -ne 0 ]; then
    echo "Error: This is not a Git repository!"
    exit 1
fi

SOURCE_DIR="$PROJECT_DIR/src"
TARGET_DIR="$PROJECT_DIR/target"
JAR_NAME="your-jar-file.jar"

# Navigate to the project directory
cd $PROJECT_DIR

# Clean up old files, if any
echo "Cleaning old build files..."
mvn clean

# Compile and package the project
echo "Building the JAR file..."
mvn package

# Check if the JAR file is created
if [ -f "$TARGET_DIR/$JAR_NAME" ]; then
    echo "JAR file created successfully: $TARGET_DIR/$JAR_NAME"
else
    echo "Error: JAR file creation failed!"
    exit 1
fi

# Optionally, copy the JAR file to a specific directory
# cp "$TARGET_DIR/$JAR_NAME" /path/to/your/desired/location

echo "Build process completed successfully!"
