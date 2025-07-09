package com.hamroyatra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        try {
            logger.info("Checking database schema...");

            // Check if special_requirements column exists in bookings table
            boolean columnExists = false;
            try {
                Integer count = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE " +
                                "TABLE_NAME = 'bookings' AND COLUMN_NAME = 'special_requirements'",
                        Integer.class
                );
                columnExists = (count != null && count > 0);

                // Add the column if it doesn't exist
                if (!columnExists) {
                    logger.info("Adding special_requirements column to bookings table");
                    jdbcTemplate.execute("ALTER TABLE bookings ADD COLUMN special_requirements TEXT");
                    logger.info("Column added successfully");
                } else {
                    logger.info("special_requirements column already exists");
                }

            } catch (Exception e) {
                logger.severe("Error initializing database: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.severe("Error during database initialization: " + e.getMessage());
        }
    }
}