CREATE TABLE films (
    id BIGSERIAL PRIMARY KEY,
    film_id BIGINT UNIQUE NOT NULL,
    film_name VARCHAR(255) NOT NULL,
    release_year INT,
    rating NUMERIC(3,1),
    description TEXT,
    created_at TIMESTAMP DEFAULT now()
);
