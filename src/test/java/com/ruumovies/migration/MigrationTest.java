package com.ruumovies.migration;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class MigrationTest {
    private static final String TEST_DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // In-memory database
    private static final String TEST_DB_USER = "sa";
    private static final String TEST_DB_PASSWORD = "";

    private static Flyway flyway;

    @BeforeAll
    static void setup() {
        try {
            // Configure Flyway for the test database
            flyway = Flyway.configure()
                    .dataSource(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD)
                    .locations("classpath:db/migration") // Path to your migration scripts
                    .load();

            // Ensure database is clean before starting
            flyway.clean();
        } catch (Exception e) {
            fail("Failed to set up Flyway: " + e.getMessage());
        }
    }

    @Test
    void testMigration() {
        assertDoesNotThrow(() -> {
            // Apply migrations
            flyway.migrate();
            System.out.println("Migrations applied successfully.");

            // Verify migrations in the database
            try (Connection connection = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD)) {
                ResultSet resultSet = connection.createStatement().executeQuery("SELECT COUNT(*) AS count FROM flyway_schema_history");
                assertTrue(resultSet.next(), "flyway_schema_history table should exist.");
                int migrationCount = resultSet.getInt("count");
                assertTrue(migrationCount > 0, "At least one migration should be applied.");
            }
        });
    }

    @AfterAll
    static void cleanup() {
        // Clean up the test database
        flyway.clean();
        System.out.println("Test database cleaned.");
    }
}
