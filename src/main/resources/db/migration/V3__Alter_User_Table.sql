-- Drop the existing users table if it exists
DROP TABLE IF EXISTS users;

-- Create user_types table
CREATE TABLE IF NOT EXISTS user_types (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL UNIQUE
);

-- Create users table with the new structure
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL UNIQUE, -- Unique user identifier
    username VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    mobile VARCHAR(15) NOT NULL UNIQUE, -- Assuming mobile numbers are unique
    user_type_id INT NOT NULL,
    joined_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_type_id) REFERENCES user_types(id) ON DELETE CASCADE
);

-- Insert initial user types
INSERT INTO user_types (type_name) VALUES
    ('super_admin'),
    ('admin'),
    ('regular'),
    ('guest');

-- Insert sample data into the new users table
INSERT INTO users (user_id, username, first_name, mobile, user_type_id)
VALUES
    ('user123', 'superadmin', 'Super Admin User', '1234567890', 1), -- Super Admin
    ('user124', 'admin', 'Admin User', '1234567891', 2),           -- Admin
    ('user125', 'regular_user', 'Regular User', '1234567892', 3),  -- Regular User
    ('user126', 'guest_user', 'Guest User', '1234567893', 4);      -- Guest
