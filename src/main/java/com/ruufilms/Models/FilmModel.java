package com.ruufilms.Models;

import com.ruufilms.Beans.Film;
import com.ruufilms.config.DatabaseConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FilmModel {

    private Map<String, Film> filmsCache = new HashMap<>();

    public void loadFilmsIntoCache() {
        String query = "SELECT f.film_id, f.film_name, f.producer, f.link, f.year, f.franchise_id, g.name AS genre " +
                "FROM films f " +
                "LEFT JOIN films_has_genres fhg ON f.film_id = fhg.films_film_id " +
                "LEFT JOIN genres g ON fhg.genres_id = g.id";

        try {
            ResultSet resultSet = DatabaseConfig.executeQuery(query);

            while (resultSet.next()) {
                String filmId = resultSet.getString("film_id");
                String filmName = resultSet.getString("film_name");
                String producer = resultSet.getString("producer");
                String link = resultSet.getString("link");
                String year = resultSet.getString("year");
                int franchiseId = resultSet.getInt("franchise_id");
                String genre = resultSet.getString("genre");

                Film film = filmsCache.get(filmId);
                if (film == null) {
                    film = new Film(filmId, filmName, producer, link, year, franchiseId);
                    filmsCache.put(filmId, film);  // Add new film to the cache
                }

                // Add genres to the film object (many-to-one relationship)
                if (genre != null) {
                    film.addGenre(genre);
                }
            }

            DatabaseConfig.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Film> getAllFilms() {
        return filmsCache;
    }

    public Film getFilmById(String filmId) {
        return filmsCache.get(filmId);
    }

    public Film getFilmByName(String filmName) {
        for (Film film : filmsCache.values()) {
            if (film.getFilmName().equalsIgnoreCase(filmName)) {
                return film;
            }
        }
        return null;
    }

}
