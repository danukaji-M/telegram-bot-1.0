package com.ruufilms.migration;

import io.github.cdimascio.dotenv.Dotenv;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

public class Migration {
    public Migration() {
        try {
            Dotenv dotenv = Dotenv.load();
            String URL = dotenv.get("DB_HOST");
            String USER = dotenv.get("DB_USER");
            String PASSWORD = dotenv.get("DB_PASSWORD");

            Flyway flyway = Flyway.configure()
                    .dataSource(URL, USER, PASSWORD)
                    .locations("classpath:db/migration")
                    .load();

            flyway.validate();  // Validate migrations before running
            flyway.migrate();
            System.out.println("Migration completed successfully.");
        } catch (FlywayException e) {
            System.err.println("Error during migration: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Migration();
    }
}
