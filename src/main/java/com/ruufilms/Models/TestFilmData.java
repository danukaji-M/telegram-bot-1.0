package com.ruufilms.Models;

import com.ruufilms.Beans.Film;

import java.util.HashMap;
import java.util.Map;

public class TestFilmData {
    public static void main(String[] args) {
        // Cache for storing films
        Map<String, Film> filmsCache = new HashMap<>();

        // Add 20 films to the cache
        for (int i = 1; i <= 20; i++) {
            String filmId = "F" + i;
            Film film = new Film(
                    filmId,
                    "FilmName" + i,
                    "Producer" + i,
                    "http://link" + i + ".com",
                    "20" + (10 + i),
                    i % 5  // Franchise ID cycling through 0-4
            );
            film.addGenre("Genre" + (i % 3)); // Add genre based on modulo (3 unique genres)
            filmsCache.put(filmId, film);
        }

        // Search example
        searchByFilmName(filmsCache, "FilmName5");
        searchByGenre(filmsCache, "Genre2");
    }

    // Search by film name
    public static void searchByFilmName(Map<String, Film> filmsCache, String filmName) {
        System.out.println("\nSearching by film name: " + filmName);
        filmsCache.values().stream()
                .filter(film -> film.getFilmName().equalsIgnoreCase(filmName))
                .forEach(System.out::println);
    }

    // Search by genre
    public static void searchByGenre(Map<String, Film> filmsCache, String genre) {
        System.out.println("\nSearching by genre: " + genre);
        filmsCache.values().stream()
                .filter(film -> film.getGenres().contains(genre))
                .forEach(System.out::println);
    }
}
