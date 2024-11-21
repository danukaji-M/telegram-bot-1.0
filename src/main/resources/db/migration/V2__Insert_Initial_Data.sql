-- Insert Initial Categories
INSERT INTO categories (name) VALUES
    ('Action'),
    ('Drama'),
    ('Comedy'),
    ('Sci-Fi'),
    ('Horror');

-- Insert Initial Users
INSERT INTO users (username, name, user_type)
VALUES
    ('superadmin', 'Super Admin User', 'super_admin'),
    ('admin', 'Admin User', 'admin'),
    ('regular_user', 'Regular User', 'regular'),
    ('guest_user', 'Guest User', 'guest');

-- Insert Initial Films
INSERT INTO films (name, file_id, uploaded_day)
VALUES
    ('Film A', 'file123', '2024-01-01'),
    ('Film B', 'file124', '2024-01-05');

-- Associate Films with Categories
INSERT INTO film_category (film_id, category_id)
VALUES
    (1, 1),  -- Film A -> Action
    (1, 2),  -- Film A -> Drama
    (2, 3);  -- Film B -> Comedy

-- Insert Initial TV Series
INSERT INTO tv_series (name, season_count, episode_count, release_date)
VALUES
    ('Series A', 3, 30, '2023-12-01'),
    ('Series B', 5, 50, '2024-03-01');

-- Associate TV Series with Categories
INSERT INTO tv_series_category (tv_series_id, category_id)
VALUES
    (1, 1),  -- Series A -> Action
    (1, 4),  -- Series A -> Sci-Fi
    (2, 2),  -- Series B -> Drama
    (2, 5);  -- Series B -> Horror
