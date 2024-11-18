#!/bin/bash

# MySQL credentials
MYSQL_USER="your_mysql_user"
MYSQL_PASSWORD="your_mysql_password"
MYSQL_DATABASE="your_database_name"
MYSQL_HOST="localhost"

# Google Cloud Storage details
GCS_BUCKET="gs://your-backup-bucket-name"
DATE=$(date +'%Y-%m-%d_%H-%M-%S')
BACKUP_FILE="backup_$MYSQL_DATABASE_$DATE.sql"

# Create backup using mysqldump
echo "Starting backup of MySQL database: $MYSQL_DATABASE"
mysqldump -h $MYSQL_HOST -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE > /tmp/$BACKUP_FILE

# Check if the backup was created successfully
if [ $? -eq 0 ]; then
    echo "Backup created successfully. Uploading to Google Cloud Storage..."

    # Upload the backup to Google Cloud Storage using gsutil
    gsutil cp /tmp/$BACKUP_FILE $GCS_BUCKET/$BACKUP_FILE

    if [ $? -eq 0 ]; then
        echo "Backup uploaded successfully to $GCS_BUCKET/$BACKUP_FILE"
        # Optionally, delete the local backup after upload
        rm /tmp/$BACKUP_FILE
    else
        echo "Failed to upload backup to Google Cloud Storage"
    fi
else
    echo "Backup failed"
fi
