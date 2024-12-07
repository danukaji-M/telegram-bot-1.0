package com.ruufilms.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruufilms.Beans.TvSeries;
import com.ruufilms.config.RedisConfig;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TvSeriesCache {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Save TV Series to Redis
    public static void saveTvSeries(TvSeries tvSeries) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            // Serialize TV Series object to JSON
            String tvSeriesJson = objectMapper.writeValueAsString(tvSeries);

            // Store TV Series by ID
            jedis.set("tvSeries:" + tvSeries.getId(), tvSeriesJson);

            // Create indexes
            jedis.sadd("tvSeries:index:groupId:" + tvSeries.getGroupId(), String.valueOf(tvSeries.getId()));
            jedis.zadd("tvSeries:index:imdb", tvSeries.getImdb(), String.valueOf(tvSeries.getId()));
            jedis.sadd("tvSeries:index:director:" + tvSeries.getDirector(), String.valueOf(tvSeries.getId()));

            // Index by genres
            if (tvSeries.getGenres() != null) {
                for (String genre : tvSeries.getGenres()) {
                    jedis.sadd("tvSeries:index:genre:" + genre.toLowerCase(), String.valueOf(tvSeries.getId()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Search by IMDb range
    public static List<TvSeries> searchByImdbRange(double min, double max) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            // Fetch TV Series IDs within the IMDb range
            List<String> tvSeriesIds = jedis.zrangeByScore("tvSeries:index:imdb", min, max);
            return getTvSeriesFromIds(tvSeriesIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Search by genre
    public static List<TvSeries> searchByGenre(String genre) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            Set<String> tvSeriesIds = jedis.smembers("tvSeries:index:genre:" + genre.toLowerCase());
            return getTvSeriesFromIds(tvSeriesIds);
        }
    }

    // Search by director
    public static List<TvSeries> searchByDirector(String director) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            Set<String> tvSeriesIds = jedis.smembers("tvSeries:index:director:" + director);
            return getTvSeriesFromIds(tvSeriesIds);
        }
    }

    // Helper: Fetch TV Series from Redis by IDs
    private static List<TvSeries> getTvSeriesFromIds(List<String> ids) {
        List<TvSeries> tvSeriesList = new ArrayList<>();
        try (Jedis jedis = RedisConfig.getJedis()) {
            for (String id : ids) {
                String tvSeriesJson = jedis.get("tvSeries:" + id);
                if (tvSeriesJson != null) {
                    tvSeriesList.add(objectMapper.readValue(tvSeriesJson, TvSeries.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tvSeriesList;
    }
    private static List<TvSeries> getTvSeriesFromIds(Set<String> ids) {
        List<TvSeries> tvSeriesList = new ArrayList<>();
        try (Jedis jedis = RedisConfig.getJedis()) {
            for (String id : ids) {
                String tvSeriesJson = jedis.get("tvSeries:" + id);
                if (tvSeriesJson != null) {
                    tvSeriesList.add(objectMapper.readValue(tvSeriesJson, TvSeries.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tvSeriesList;
    }

    public static void main(String[] args) {
        // Create sample TV Series and save them
        TvSeries series1 = createTvSeries(1, "Breaking Bad", "link1", "2008", "2023", 9.5f, "Vince Gilligan", List.of("Crime", "Drama", "Thriller"));
        TvSeries series2 = createTvSeries(2, "Game of Thrones", "link2", "2011", "2023", 9.2f, "David Benioff", List.of("Drama", "Fantasy", "Action"));
        TvSeries series3 = createTvSeries(3, "Stranger Things", "link3", "2016", "2023", 7.1f, "The Duffer Brothers", List.of("Drama", "Fantasy", "Sci-Fi"));

        saveTvSeries(series1);
        saveTvSeries(series2);
        saveTvSeries(series3);

        // Test searches
        System.out.println("Search by IMDb range 8.0 to 9.5:");
        List<TvSeries> tvSeriesByImdb = searchByImdbRange(8.0, 9.5);
        tvSeriesByImdb.forEach(s -> System.out.println(" - " + s.getGroupId()));

        System.out.println("\nSearch by genre Drama:");
        List<TvSeries> tvSeriesByGenre = searchByGenre("Drama");
        tvSeriesByGenre.forEach(s -> System.out.println(" - " + s.getGroupId()));

        System.out.println("\nSearch by director Vince Gilligan:");
        List<TvSeries> tvSeriesByDirector = searchByDirector("Vince Gilligan");
        tvSeriesByDirector.forEach(s -> System.out.println(" - " + s.getGroupId()));
    }

    // Helper to create sample TV Series
    private static TvSeries createTvSeries(int id, String groupId, String groupLink, String released, String addedYear, float imdb, String director, List<String> genres) {
        TvSeries tvSeries = new TvSeries();
        tvSeries.setId(id);
        tvSeries.setGroupId(groupId);
        tvSeries.setGroupLink(groupLink);
        tvSeries.setReleased(released);
        tvSeries.setAddedYear(addedYear);
        tvSeries.setImdb(imdb);
        tvSeries.setDirector(director);
        tvSeries.setGenres(genres);
        return tvSeries;
    }
}
