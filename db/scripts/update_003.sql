ALTER TABLE price_history ADD COLUMN post_id INT REFERENCES auto_posts(id);