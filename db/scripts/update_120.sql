ALTER TABLE auto_drivers ADD COLUMN user_id INT NOT NULL REFERENCES auto_users(id);
