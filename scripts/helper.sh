#!/bin/bash

# Script to install and run the Telegram Bot API server with error handling

# Constants
API_ID="24150008"
API_HASH="b63bf4acbec8984467850b3474d218ef"
HTTP_PORT="8081"
BUILD_DIR="telegram-bot-api/build"
ROOT_DIR=$(pwd)

# Step 1: Install Telegram Bot API server
echo "Starting the installation of Telegram Bot API server..."

# Ensure necessary dependencies are installed
sudo apt-get update && sudo apt-get upgrade -y
sudo apt-get install -y make git zlib1g-dev libssl-dev gperf cmake clang-18 libc++-18-dev libc++abi-18-dev

# Clone the repository if not already present
if [ ! -d "telegram-bot-api" ]; then
    git clone --recursive https://github.com/tdlib/telegram-bot-api.git
fi

# Navigate to the directory and build the binary
cd telegram-bot-api || { echo "Failed to navigate to telegram-bot-api directory"; exit 1; }
rm -rf "$BUILD_DIR"
mkdir "$BUILD_DIR"
cd "$BUILD_DIR" || { echo "Failed to navigate to build directory"; exit 1; }

# Build the Telegram Bot API binary
CXXFLAGS="-stdlib=libc++" CC=/usr/bin/clang-18 CXX=/usr/bin/clang++-18 cmake -DCMAKE_BUILD_TYPE=Release -DCMAKE_INSTALL_PREFIX:PATH=.. ..
cmake --build . --target install

# Retrieve the binary path
TELEGRAM_API_BINARY=$(realpath ../bin/telegram-bot-api)

# Return to the root directory
cd ../../..

# Step 2: Write the binary path to .env file in the root directory
echo "Writing installation path to .env..."
echo "TELEGRAM_LOCAL_SERVER_INSTALLATION_PATH=$TELEGRAM_API_BINARY" > "$ROOT_DIR/.env"

# Step 3: Start Telegram Bot API server
echo "Starting Telegram Bot API server with the following configuration:"
echo "----------------------------------------"
echo "API ID       : $API_ID"
echo "API Hash     : $API_HASH"
echo "HTTP Port    : $HTTP_PORT"
echo "Server URL   : http://localhost:$HTTP_PORT"
echo "----------------------------------------"

# Check if the Telegram API binary exists
if [ ! -f "$TELEGRAM_API_BINARY" ]; then
    echo "Error: Telegram Bot API binary not found at '$TELEGRAM_API_BINARY'."
    echo "Please check the path and ensure the file exists."
    exit 1
fi

# Check if the binary is executable
if [ ! -x "$TELEGRAM_API_BINARY" ]; then
    echo "Making the binary executable..."
    chmod +x "$TELEGRAM_API_BINARY"
fi

# Run the Telegram Bot API server
"$TELEGRAM_API_BINARY" --api-id=$API_ID --api-hash=$API_HASH --http-port=$HTTP_PORT
EXIT_CODE=$?

# Check the exit code of the Telegram Bot API server
if [ $EXIT_CODE -ne 0 ]; then
    echo "Error: Telegram Bot API server failed to start. Exit code: $EXIT_CODE"
    exit $EXIT_CODE
fi

echo "Telegram Bot API server stopped successfully on http://localhost:$HTTP_PORT"
