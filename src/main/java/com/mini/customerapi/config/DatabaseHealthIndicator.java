package com.mini.customerapi.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    // Inject JdbcTemplate to access the database
    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        try {
            // Run a simple query to check the database status
            jdbcTemplate.execute("SELECT 1");
            return Health.up().withDetail("Database", "Available").build();
        } catch (Exception e) {
            // If an exception occurs, mark the health as down
            return Health.down().withDetail("Database", "Not reachable")
                    .withDetail("Error", e.getMessage()).build();
        }
    }
}