#!/bin/bash

NC='\033[0m'
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
MAGENTA='\033[0;35m'
GRAY='\033[1;30m'
WHITE='\033[1;37m'

print_menu(){
  clear
  echo -e "${BLUE}************************************************************"
  echo -e "${BLUE}*             RUU FILM BOT INSTALLATION                    *"
  echo -e "${BLUE}************************************************************"
  echo -e "${YELLOW}* Created By: Danukaji Hansanath                           *"
  echo -e "${YELLOW}* Github: https://github.com/danukaji-M                    *"
  echo -e "${YELLOW}* For any issues, contact: danukajihansanath0408@gmail.com *"
  echo -e "${BLUE}************************************************************"
  echo -e "${BLUE}"
  echo "RRRR   U   U  U   U     M     M  OOO   V   V  III  EEEEE  SSSSS  "
  echo "R   R  U   U  U   U     MM   MM O   O  V   V   I   E      S      "
  echo "RRRR   U   U  U   U     M M M M O   O  V   V   I   EEEE   SSSSS  "
  echo "R  R   U   U  U   U     M  M  M O   O  V   V   I   E          S  "
  echo "R   R  UUUUU  UUUUU     M     M  OOO    VVV   III  EEEEE  SSSSS  "
  echo -e "${NC}"
}

print_menu

PASSWORD_FILE="credentials.txt"
MYSQL_ROOT_PASSWORD=$(< /dev/urandom tr -dc 'A-Za-z9' | head -c 16)
REDIS_PASSWORD="admin"
REDIS_USER="film_bot"

echo -e "${MAGENTA} **************************************************"
echo -e "${CYAN} ********STARTING SYSTEM UPDATE AND UPGRADE********"
echo -e "${MAGENTA} **************************************************"
echo -e "${NC}"

sudo apt-get update -y && sudo apt-get upgrade -y

echo -e "${MAGENTA} **************************************************"
echo -e "${CYAN} **********STARTING THE MYSQL INSTALLATION*********"
echo -e "${MAGENTA} **************************************************"
echo -e "${NC}"

sudo apt-get install expect -y
sudo apt install mysql-server -y
sudo apt install -y expect

if mysql --version &>/dev/null; then
    echo -e "${CYAN}MySQL is already installed. Please enter the root password to proceed.${NC}"
    read -sp "Enter MySQL root password: " MYSQL_ROOT_PASSWORD
    echo
else
    SECURE_MYSQL_SCRIPTS=$(expect -c"
    spawn sudo mysql_secure_installation

    expect \"Enter current password for root (enter for none):\"
    send \"$MYSQL_ROOT_PASSWORD\r\"

    expect \"Set root password?\"
    send \"y\r\"

    expect \"New password:\"
    send \"$MYSQL_ROOT_PASSWORD\r\"

    expect \"Re-enter new password:\"
    send \"$MYSQL_ROOT_PASSWORD\r\"

    expect \"Remove anonymous users?\"
    send \"y\r\"

    expect \"Disallow root login remotely?\"
    send \"y\r\"

    expect \"Remove test database and access to it?\"
    send \"y\r\"

    expect \"Reload privilege tables now?\"
    send \"y\r\"

    expect eof
    ")
    echo -e "${RED}Generated Password: $MYSQL_ROOT_PASSWORD"
    echo -e "${NC}"
fi

if [ -f "$PASSWORD_FILE" ]; then
    rm -f "$PASSWORD_FILE"
fi

echo "MYSQL_USER_NAME=root" > "$PASSWORD_FILE"
echo "MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD\n" >> "$PASSWORD_FILE"
echo "REDIS_USER=$REDIS_USER" >> "$PASSWORD_FILE"
echo "REDIS_PASSWORD=$REDIS_PASSWORD\n" >> "$PASSWORD_FILE"
chmod 600 "$PASSWORD_FILE"

echo -e "${CYAN}Creating new user 'film_bot' and films database...${NC}"
sudo mysql -u root -p <<EOF
CREATE DATABASE IF NOT EXISTS films;
CREATE USER IF NOT EXISTS 'film_bot'@'localhost' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON films.* TO 'film_bot'@'localhost';
FLUSH PRIVILEGES;
EOF

if sudo mysql -u root -p -e 'SHOW DATABASES;' | grep -q 'films'; then
    echo -e "${MAGENTA} **************************************************"
    echo -e "${GREEN} **********MYSQL INSTALLATION SUCCESSFUL!*********"
    echo -e "${MAGENTA} **************************************************"
    echo -e "${CYAN} 'film_bot' user created and 'films' database set up successfully!${NC}"
    echo -e "${MAGENTA} **************************************************"
else
    echo -e "${RED}Failed to create database or user.${NC}"
fi

echo -e "${MAGENTA} **************************************************"
echo -e "${CYAN} **********STARTING THE REDIS INSTALLATION*********"
echo -e "${MAGENTA} **************************************************"
echo -e "${NC}"
sudo apt-get install redis-server -y

echo -e "${MAGENTA} **************************************************"
echo -e "${CYAN} **********CONFIGURING REDIS PASSWORD*********"
echo -e "${MAGENTA} **************************************************"
echo -e "${NC}"

sudo sed -i "s/^# requirepass .*/requirepass $REDIS_PASSWORD/" /etc/redis/redis.conf
sudo systemctl restart redis.service

if systemctl status redis | grep "active (running)" &>/dev/null; then
    echo -e "${GREEN}Redis server is running and password set successfully!${NC}"
else
    echo -e "${RED}Redis server failed to start.${NC}"
fi

read -p "Do you want to delete the 'film_bot' user, 'films' database, and Redis data? (y/n): " DELETE_OPTION

if [[ "$DELETE_OPTION" == "y" || "$DELETE_OPTION" == "Y" ]]; then
    echo -e "${CYAN}Deleting 'film_bot' user and 'films' database...${NC}"
    sudo mysql -u root -p <<EOF
    DROP USER IF EXISTS 'film_bot'@'localhost';
    DROP DATABASE IF EXISTS films;
    FLUSH PRIVILEGES;
EOF
    echo -e "${GREEN}'film_bot' user and 'films' database have been deleted.${NC}"

    echo -e "${CYAN}Flushing Redis data...${NC}"
    redis-cli -a $REDIS_PASSWORD FLUSHALL
    echo -e "${GREEN}Redis data flushed successfully.${NC}"
else
    echo -e "${GREEN}No changes made. The 'film_bot' user, 'films' database, and Redis data are still intact.${NC}"
fi

echo -e "${NC}"
