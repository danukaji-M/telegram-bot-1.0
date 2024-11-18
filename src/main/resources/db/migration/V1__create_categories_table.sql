-- V1__Initial_migration.sql

-- Create categories table
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    user_type ENUM('admin', 'regular', 'guest') NOT NULL,
    joined_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Create films table with a foreign key reference to categories
CREATE TABLE films (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    file_id VARCHAR(255),
    uploaded_day DATE,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Create flyway_schema_history table (if necessary)
CREATE TABLE flyway_schema_history (
    installed_rank INT NOT NULL,
    version VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    type VARCHAR(20) NOT NULL,
    script VARCHAR(1000) NOT NULL,
    checksum INT,
    installed_by VARCHAR(100),
    installed_on TIMESTAMP NOT NULL,
    execution_time INT,
    success BOOLEAN NOT NULL,
    PRIMARY KEY (installed_rank)
);

-- Optionally, seed initial data
INSERT INTO categories (name) VALUES ('Action'), ('Drama'), ('Comedy');
INSERT INTO users (username, name, user_type) VALUES ('admin', 'Admin User', 'admin');
