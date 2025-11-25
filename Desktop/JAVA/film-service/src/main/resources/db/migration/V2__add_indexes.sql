CREATE INDEX IF NOT EXISTS idx_films_release_year
    ON films (release_year);

CREATE INDEX IF NOT EXISTS idx_films_film_name
    ON films (film_name);

CREATE INDEX IF NOT EXISTS idx_films_film_id
    ON films (film_id);

