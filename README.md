# telegram-bot

Welcome to the Telegram Bot project! This bot is built using Java and integrates several technologies, such as the Telegram Bot API, jlib Torrent, MySQL, Redis, and Google Backup, for a fully automated experience.

### Technologies Used
- **Telegram Bot API**: Powers the core functionality of the bot.
- **jlib Torrent**: Used for torrent management and file handling.
- **MySQL**: A relational database used for storing bot data.
- **Redis**: Provides caching to improve performance.
- **Google Backup**: For automatic cloud-based backup of critical files.

### Features
- **Automated Torrent Management**: Manage torrents using jlib Torrent library.
- **Database Management**: Stores and retrieves bot data with MySQL.
- **Redis Caching**: Improves performance by caching frequently accessed data.
- **Google Backup Integration**: Backs up important files and data to Google Drive.
- **User Interaction**: Interact with the bot via Telegram to manage torrents, backups, and data.

## Installation

### 1. Clone the Repository
Clone the repository to your local machine:
```bash
git clone https://github.com/danukaji-M/telegram-bot-1.0.git
cd telegram-bot

```
### 2. Set Up Java Project

This project is built using **Maven**. Make sure you have **Maven** installed on your machine. To set up the project, run:

```bash
mvn clean install
```
### 3. Set Up Environment Variables

Create a `.env` file in the project root directory and configure your environment variables as follows:

```dotenv
TELEGRAM_BOT_API_KEY=your_telegram_bot_api_key
MYSQL_HOST=your_mysql_host
MYSQL_USER=your_mysql_user
MYSQL_PASSWORD=your_mysql_password
MYSQL_DATABASE=your_mysql_database
REDIS_HOST=your_redis_host
REDIS_PORT=your_redis_port
GOOGLE_BACKUP_API_KEY=your_google_backup_api_key
```

Make sure to replace each placeholder with your actual API keys and credentials:

- **TELEGRAM_BOT_API_KEY** : Your Telegram bot's API key.
- **MYSQL_HOST** : The host of your MySQL database.
- **MYSQL_USER** : Your MySQL username.
- **MYSQL_PASSWORD** : Your MySQL password.
- **MYSQL_DATABASE** : Your MySQL database name.
- **REDIS_HOST**: The host of your Redis server.
- **REDIS_PORT** : The port for your Redis server (usually 6379).
- **GOOGLE_BACKUP_API_KEY** : The API key for Google Backup integration.

