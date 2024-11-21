-- Create flyway_schema_history table (if necessary)
CREATE TABLE IF NOT EXISTS flyway_schema_history (
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

-- Create User Table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    user_type ENUM('super_admin', 'admin', 'regular', 'guest') NOT NULL,
    joined_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Create Category Table
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create Film Table
CREATE TABLE IF NOT EXISTS films (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    file_id VARCHAR(255),
    uploaded_day DATE
);

-- Create Film Category Junction Table
CREATE TABLE IF NOT EXISTS film_category (
    film_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (film_id, category_id),
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- Create TV Series Table
CREATE TABLE IF NOT EXISTS tv_series (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    season_count INT NOT NULL,
    episode_count INT NOT NULL,
    release_date DATE
);

-- Create TV Series Category Junction Table
CREATE TABLE IF NOT EXISTS tv_series_category (
    tv_series_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (tv_series_id, category_id),
    FOREIGN KEY (tv_series_id) REFERENCES tv_series(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);
