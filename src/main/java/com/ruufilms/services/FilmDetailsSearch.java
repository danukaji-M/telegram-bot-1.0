package com.ruufilms.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FilmDetailsSearch {
    public Map<String, Object> FilmData(String name) throws IOException {
        // Build the API URL with the movie name and API key
        String urlString = "http://omdbapi.com/?t=" + name.replace(" ", "+") + "&apikey=a6c592ab";
        System.out.println("Requesting URL: " + urlString); // Debugging
        URL url = new URL(urlString);

        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode); // Debugging

        Map<String, Object> movieMap = new HashMap<>();

        if (responseCode == HttpURLConnection.HTTP_OK) { // Check if the response code is 200 (OK)
            // Read the response from the input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print out the raw response to check the structure (for debugging)
            System.out.println("Raw Response: " + response);

            // Parse the response as a JSONObject
            JSONObject myResponse = new JSONObject(response.toString());

            // Check if the response is valid
            if ("True".equals(myResponse.optString("Response", "False"))) {
                // Populate the movieMap with the fields from the response
                movieMap.put("Title", myResponse.optString("Title", "N/A"));
                movieMap.put("Year", myResponse.optString("Year", "N/A"));
                movieMap.put("Rated", myResponse.optString("Rated", "N/A"));
                movieMap.put("Released", myResponse.optString("Released", "N/A"));
                movieMap.put("Runtime", myResponse.optString("Runtime", "N/A"));
                movieMap.put("Genre", myResponse.optString("Genre", "N/A"));
                movieMap.put("Director", myResponse.optString("Director", "N/A"));
                movieMap.put("Writer", myResponse.optString("Writer", "N/A"));
                movieMap.put("Actors", myResponse.optString("Actors", "N/A"));
                movieMap.put("Plot", myResponse.optString("Plot", "N/A"));
                movieMap.put("Language", myResponse.optString("Language", "N/A"));
                movieMap.put("Country", myResponse.optString("Country", "N/A"));
                movieMap.put("Awards", myResponse.optString("Awards", "N/A"));
                movieMap.put("Poster", myResponse.optString("Poster", "N/A"));
                movieMap.put("Metascore", myResponse.optString("Metascore", "N/A"));
                movieMap.put("imdbRating", myResponse.optString("imdbRating", "N/A"));
                movieMap.put("imdbVotes", myResponse.optString("imdbVotes", "N/A"));
                movieMap.put("imdbID", myResponse.optString("imdbID", "N/A"));
                movieMap.put("Type", myResponse.optString("Type", "N/A"));
                movieMap.put("DVD", myResponse.optString("DVD", "N/A"));
                movieMap.put("BoxOffice", myResponse.optString("BoxOffice", "N/A"));
                movieMap.put("Production", myResponse.optString("Production", "N/A"));
                movieMap.put("Website", myResponse.optString("Website", "N/A"));

                // Extract the Ratings array
                JSONArray ratingsArray = myResponse.optJSONArray("Ratings");
                Map<String, String> ratingsMap = new HashMap<>();
                if (ratingsArray != null) {
                    for (int i = 0; i < ratingsArray.length(); i++) {
                        JSONObject rating = ratingsArray.getJSONObject(i);
                        ratingsMap.put(rating.optString("Source", "N/A"), rating.optString("Value", "N/A"));
                    }
                }
                movieMap.put("Ratings", ratingsMap);

            } else {
                // Log the error if the response is invalid
                String error = myResponse.optString("Error", "Unknown error");
                System.err.println("OMDB API Error: " + error);
                movieMap.put("Error", error);
            }

        } else {
            System.err.println("Failed to get a valid response. HTTP Code: " + responseCode);
        }

        return movieMap; // Return the populated movie map
    }

    public static void main(String[] args) {
        try {
            FilmDetailsSearch search = new FilmDetailsSearch();
            Map<String, Object> movieDetails = search.FilmData("Inception");

            // Print the movie details
            System.out.println("\nReturned Movie Details:");
            for (Map.Entry<String, Object> entry : movieDetails.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//ksxLZDGaE4l6cwpninyynyqVkrWaCCZNdBnVjyV90Wj2NBneT6ub1WOh