ALTER TABLE auto_posts ADD COLUMN car_id INT UNIQUE REFERENCES auto_cars(id);

