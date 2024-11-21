-- Create Type Table (for 'user', 'main', 'backup') - This should come first
CREATE TABLE IF NOT EXISTS types (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type_name ENUM('user', 'main', 'backup') NOT NULL
);

-- Create Group Table (with a foreign key to Type) - This should come after the `types` table
CREATE TABLE IF NOT EXISTS groups_channels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    group_id VARCHAR(255) NOT NULL UNIQUE,
    group_name VARCHAR(255) NOT NULL,
    type_id INT,  -- Foreign key referencing the Type table
    FOREIGN KEY (type_id) REFERENCES types(id) ON DELETE SET NULL
);
