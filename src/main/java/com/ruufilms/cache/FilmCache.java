package com.ruufilms.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruufilms.Beans.Film;
import com.ruufilms.config.RedisConfig;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FilmCache {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void saveFilm(Film film) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            // Serialize Film object to JSON
            String filmJson = objectMapper.writeValueAsString(film);

            // Store film by ID
            jedis.set("film:" + film.getId(), filmJson);

            // Create indexes
            jedis.sadd("film:index:name:" + film.getFilmName(), String.valueOf(film.getId()));
            jedis.zadd("film:index:imdb", film.getImdbRate(), String.valueOf(film.getId()));
            jedis.sadd("film:index:director:" + film.getDirector(), String.valueOf(film.getId()));

            // Index by genres
            if (film.getGenres() != null) {
                for (String genre : film.getGenres()) {
                    jedis.sadd("film:index:genre:" + genre.toLowerCase(), String.valueOf(film.getId()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Search by name
    public static List<Film> searchByName(String name) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            Set<String> filmKeys = jedis.smembers("film:index:name:"+name);
            return getFilmsFromKeys(filmKeys);
        }
    }

    public static List<Film> searchByImdbRange(double min, double max) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            // Fetch film IDs from Redis within the specified IMDb range
            List<String> filmIds = jedis.zrangeByScore("film:index:imdb", min, max);

            // Convert the list of IDs to a list of Film objects
            return getFilmsFromIds(filmIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list in case of error
        }
    }


    // Search by genre
    public static List<Film> searchByGenre(String genre) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            Set<String> filmIds = jedis.smembers("film:index:genre:" + genre.toLowerCase());
            return getFilmsFromIds(filmIds);
        }
    }

    // Search by director
    public static List<Film> searchByDirector(String director) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            Set<String> filmIds = jedis.smembers("film:index:director:" + director);
            return getFilmsFromIds(filmIds);
        }
    }

    // Helper: Fetch films from Redis by keys
    private static List<Film> getFilmsFromKeys(List<String> keys) {
        List<Film> films = new ArrayList<>();
        try (Jedis jedis = RedisConfig.getJedis()) {
            for (String key : keys) {
                String[] parts = key.split(":");
                String filmId = parts[parts.length - 1];
                String filmJson = jedis.get("film:" + filmId);
                if (filmJson != null) {
                    films.add(objectMapper.readValue(filmJson, Film.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return films;
    }

    private static List<Film> getFilmsFromKeys(Set<String> keys) {
        List<Film> films = new ArrayList<>();
        try (Jedis jedis = RedisConfig.getJedis()) {
            for (String key : keys) {
                String[] parts = key.split(":");
                String filmId = parts[parts.length - 1];
                String filmJson = jedis.get("film:" + filmId);
                if (filmJson != null) {
                    films.add(objectMapper.readValue(filmJson, Film.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return films;
    }

    // Helper: Fetch films from Redis by IDs
    private static List<Film> getFilmsFromIds(List<String> ids) {
        List<Film> films = new ArrayList<>();
        try (Jedis jedis = RedisConfig.getJedis()) {
            for (String id : ids) {
                String filmJson = jedis.get("film:" + id);
                if (filmJson != null) {
                    films.add(objectMapper.readValue(filmJson, Film.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return films;
    }

    private static List<Film> getFilmsFromIds(Set<String> ids) {
        List<Film> films = new ArrayList<>();
        try (Jedis jedis = RedisConfig.getJedis()) {
            for (String id : ids) {
                String filmJson = jedis.get("film:" + id);
                if (filmJson != null) {
                    films.add(objectMapper.readValue(filmJson, Film.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return films;
    }

    public static void main(String[] args) {
        // Create and save 10 films
        Film film1 = createFilm(1, "Inception", "file1", "2010", 8.8f, "Christopher Nolan", List.of("Sci-Fi", "Thriller"));
        Film film2 = createFilm(2, "The Dark Knight", "file2", "2008", 9.0f, "Christopher Nolan", List.of("Action", "Crime"));
        Film film3 = createFilm(3, "Interstellar", "file3", "2014", 8.6f, "Christopher Nolan", List.of("Sci-Fi", "Drama"));
        Film film4 = createFilm(4, "Dunkirk", "file4", "2017", 7.9f, "Christopher Nolan", List.of("War", "Drama"));
        Film film5 = createFilm(5, "Pulp Fiction", "file5", "1994", 8.9f, "Quentin Tarantino", List.of("Crime", "Drama"));
        Film film6 = createFilm(6, "Kill Bill: Vol. 1", "file6", "2003", 8.1f, "Quentin Tarantino", List.of("Action", "Thriller"));
        Film film7 = createFilm(7, "The Matrix", "file7", "1999", 8.7f, "Lana Wachowski", List.of("Sci-Fi", "Action"));
        Film film8 = createFilm(8, "Fight Club", "file8", "1999", 8.8f, "David Fincher", List.of("Drama", "Thriller"));
        Film film9 = createFilm(9, "Forrest Gump", "file9", "1994", 8.8f, "Robert Zemeckis", List.of("Drama", "Romance"));
        Film film10 = createFilm(10, "The Shawshank Redemption", "file10", "1994", 9.3f, "Frank Darabont", List.of("Drama", "Crime"));

        // Save all films to Redis
        FilmCache.saveFilm(film1);
        FilmCache.saveFilm(film2);
        FilmCache.saveFilm(film3);
        FilmCache.saveFilm(film4);
        FilmCache.saveFilm(film5);
        FilmCache.saveFilm(film6);
        FilmCache.saveFilm(film7);
        FilmCache.saveFilm(film8);
        FilmCache.saveFilm(film9);
        FilmCache.saveFilm(film10);

        // Test searches
        System.out.println("Search by name:");
        List<Film> filmsByName = FilmCache.searchByName("Inception");
        filmsByName.forEach(f -> System.out.println(" - " + f.getFilmName()));

        System.out.println("\nSearch by IMDb range 8.5 to 9.0:");
        List<Film> filmsByImdb = FilmCache.searchByImdbRange(7.0, 9.0);
        filmsByImdb.forEach(f -> System.out.println(" - " + f.getFilmName()));

        System.out.println("\nSearch by director Christopher Nolan:");
        List<Film> filmsByDirector = FilmCache.searchByDirector("Christopher Nolan");
        filmsByDirector.forEach(f -> System.out.println(" - " + f.getFilmName()));

        System.out.println("\nSearch by genre Sci-Fi:");
        List<Film> filmsByGenre = FilmCache.searchByGenre("Sci-Fi");
        filmsByGenre.forEach(f -> System.out.println(" - " + f.getFilmName()));
    }

    private static Film createFilm(int id, String name, String fileId, String year, float imdbRate, String director, List<String> genres) {
        Film film = new Film();
        film.setId(id);
        film.setFilmName(name);
        film.setFileId(fileId);
        film.setYear(year);
        film.setReleasedYear(year);
        film.setImdbRate(imdbRate);
        film.setDirector(director);
        film.setGenres(genres);
        return film;
    }
}
