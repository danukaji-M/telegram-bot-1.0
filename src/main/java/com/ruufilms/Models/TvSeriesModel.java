package com.ruufilms.Models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruufilms.Beans.TvSeries;
import com.ruufilms.config.DatabaseConfig;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TvSeriesModel {
    public HashMap<String, TvSeries> getAllTvSeries() {
        HashMap<String, TvSeries> tvSeriesHashMap = new HashMap<>();
        String sql = "SELECT tv_series.group_id, tv_series.name, tv_series.group_link, tv_series.year, " +
                "tv_series.create_time, genres.name AS genre_name " +
                "FROM tv_series " +
                "INNER JOIN genres_has_tv_series ON tv_series.group_id = genres_has_tv_series.tv_series_group_id " +
                "INNER JOIN genres ON genres.id = genres_has_tv_series.genres_id";

        try {
            ResultSet resultSet = DatabaseConfig.executeQuery(sql);

            while (resultSet.next()) {
                // Retrieve fields from the result set
                String groupId = resultSet.getString("group_id");
                String name = resultSet.getString("name");
                String groupLink = resultSet.getString("group_link");
                String year = resultSet.getString("year");
                String createdTime = resultSet.getString("create_time");
                String genreName = resultSet.getString("genre_name");

                // Check if the TvSeries object already exists
                TvSeries tvSeries = tvSeriesHashMap.get(groupId);

                if (tvSeries == null) {
                    // Create a new TvSeries object if it doesn't exist
                    tvSeries = new TvSeries();
                    tvSeries.setGroupId(groupId);
                    tvSeries.setName(name);
                    tvSeries.setGroupLink(groupLink);
                    tvSeries.setYear(year);
                    tvSeries.setCreatedTime(createdTime);
                    tvSeries.setGenres(new ArrayList<>());
                    tvSeriesHashMap.put(groupId, tvSeries);
                }

                tvSeries.getGenres().add(genreName);
            }

            DatabaseConfig.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tvSeriesHashMap;
    }

    public void createTvSeries(String groupId, String name, String groupLink, String year){
        String sql = "INSERT INTO tv_series(`group_id`, `name`, `group_link`, `year`, `create_time`) VALUES (?, ?, ?, ?, ?)";
        try{
            ZonedDateTime sriLankaTime = ZonedDateTime.now(ZoneId.of("Asia/Colombo"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sriLankaTime.format(formatter);
            System.out.println(formattedDate);
            int rowsAffected = DatabaseConfig.executePreparedUpdate(sql, groupId,name,groupLink,year,formattedDate);
            if(rowsAffected>0){
                System.out.println("Successfully Created");
            }else{
                System.out.println("Something went wrong");
            }
            DatabaseConfig.closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createTvHasGenres(String groupId, ArrayList<String> genres) {
        System.out.println("Start "+this.getClass() + " : " +groupId + " : " +genres.size());
        String sql = "INSERT INTO genres_has_tv_series(`tv_series_group_id`, `genres_id`) VALUES(?, ?)";
        try{

            for (String genre : genres) {
                int genreId = getGenreIdByName( genre);
                if (genreId > 0) {
                    DatabaseConfig.executePreparedUpdate(sql,groupId,genreId);
                } else {
                    System.out.println("Genre not found: " + genre);
                }
            }
            DatabaseConfig.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getGenreIdByName(String genreName) {
        String sql = "SELECT id FROM genres WHERE name = ?";
        try(ResultSet resultSet = DatabaseConfig.executePreparedQuery(sql,genreName)){
            if(resultSet.next()){
                return resultSet.getInt("id");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static void main(String[] args) {
        TvSeriesModel tvSeriesModel = new TvSeriesModel(); // Assuming this class has getAllTvSeries method
        HashMap<String, TvSeries> tvSeriesHashMap = tvSeriesModel.getAllTvSeries();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tvSeriesHashMap);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
