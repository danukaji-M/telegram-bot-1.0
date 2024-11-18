package com.ruumovies.config;

import com.ruufilms.config.DatabaseConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.sql.Connection;
import java.sql.ResultSet;


import static org.junit.jupiter.api.Assertions.*;
public class DatabaseConfigTest {
    private static Connection connection;

    @BeforeAll
    static void setup(){
        try{
            connection = DatabaseConfig.getConnection();
        }catch (Exception e){
            fail("Failed to set up database connection: " + e.getMessage());
        }
    }

    @Test
    void testConnection(){
        assertDoesNotThrow(() -> {
            assertNotNull(connection, "Connection should not be null");
            assertFalse(connection.isClosed(), "Connection should be open");
        });
    }

    @Test
    void testExecutionQuery(){
        assertDoesNotThrow(() -> {
            ResultSet resultSet = DatabaseConfig.executeQuery("SELECT 1 AS result");
            assertTrue(resultSet.next(), "Result set should have at least one row");
            assertEquals(1, resultSet.getInt("result"), "Query should return 1");
        });
    }

    @Test
    void testExecuteUpdate() {
        assertDoesNotThrow(() -> {

            String createTableQuery = "CREATE TEMPORARY TABLE test_table (id INT PRIMARY KEY)";
            int result = DatabaseConfig.executeUpdate(createTableQuery);
            assertEquals(0, result, "DDL statements should return 0");

            String insertQuery = "INSERT INTO test_table (id) VALUES (1)";
            result = DatabaseConfig.executeUpdate(insertQuery);
            assertEquals(1, result, "Insert query should affect 1 row");

            String dropTableQuery = "DROP TABLE test_table";
            DatabaseConfig.executeUpdate(dropTableQuery);
        });
    }

    @AfterAll
    static void cleanup(){
        try {
            DatabaseConfig.closeConnection();
            assertTrue(connection.isClosed(), "Connection should be closed after cleanup");
        } catch (Exception e) {
            fail("Failed to clean up database connection: " + e.getMessage());
        }
    }
}
