package com.ruumovies.Models;

import com.ruufilms.Beans.TvSeries;
import com.ruufilms.Models.TvSeriesModel;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TvSeriesModelTest {

    @Test
    public void testGetAllTvSeries() {
        TvSeriesModel tvSeriesModel = new TvSeriesModel(); // Assuming this is the class containing getAllTvSeries
        HashMap<String, TvSeries> tvSeriesHashMap = tvSeriesModel.getAllTvSeries();

        // Assertions
        assertNotNull(tvSeriesHashMap, "The result should not be null");
        assertEquals(3, tvSeriesHashMap.size(), "There should be 3 TV series in the result");

        // Verify details of group_id 1
        TvSeries group1 = tvSeriesHashMap.get("1");
        assertNotNull(group1, "Group 1 (test) should be present");
        assertEquals("test", group1.getName());
        assertEquals("test.link", group1.getGroupLink());
        assertEquals("2021-01-01", group1.getYear());
        assertEquals("2023-01-01 12:00:00", group1.getCreatedTime());
        List<String> genres1 = group1.getGenres();
        assertNotNull(genres1, "Genres list for group 1 should not be null");
        assertEquals(3, genres1.size(), "Group 1 should have 3 genres");
        assertTrue(genres1.contains("Action"), "Genres should include 'Action'");
        assertTrue(genres1.contains("Adventure"), "Genres should include 'Adventure'");
        assertTrue(genres1.contains("Comedy"), "Genres should include 'Comedy'");

        // Verify details of group_id 2
        TvSeries group2 = tvSeriesHashMap.get("2");
        assertNotNull(group2, "Group 2 (Comedy Series) should be present");
        assertEquals("Comedy Series", group2.getName());
        assertEquals("comedy.link", group2.getGroupLink());
        assertEquals("2023-01-01", group2.getYear());
        assertEquals("2023-01-01 12:00:00", group2.getCreatedTime());
        List<String> genres2 = group2.getGenres();
        assertNotNull(genres2, "Genres list for group 2 should not be null");
        assertEquals(2, genres2.size(), "Group 2 should have 2 genres");
        assertTrue(genres2.contains("Adventure"), "Genres should include 'Adventure'");
        assertTrue(genres2.contains("Animation"), "Genres should include 'Animation'");

        // Verify details of group_id 3
        TvSeries group3 = tvSeriesHashMap.get("3");
        assertNotNull(group3, "Group 3 (Action Series) should be present");
        assertEquals("Action Series", group3.getName());
        assertEquals("action.link", group3.getGroupLink());
        assertEquals("2022-01-01", group3.getYear());
        assertEquals("2022-01-01 12:00:00", group3.getCreatedTime());
        List<String> genres3 = group3.getGenres();
        assertNotNull(genres3, "Genres list for group 3 should not be null");
        assertEquals(2, genres3.size(), "Group 3 should have 2 genres");
        assertTrue(genres3.contains("Action"), "Genres should include 'Action'");
        assertTrue(genres3.contains("Comedy"), "Genres should include 'Comedy'");
    }
}
