package com.example.assetsystembackend.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Controller class for health check endpoints.
 */
@RestController
@RequestMapping("/api")
public class HealthCheckController {

    private final DataSource dataSource;

    @Autowired
    public HealthCheckController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Endpoint to check if the server and database are up.
     *
     * @return ResponseEntity with a success message if the server and database are up.
     */
    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        // Check if the database is connected
        if (isDatabaseConnected()) {
            return ResponseEntity.ok("Server and database are up and running!");
        }
        return ResponseEntity.status(500).body("Database connection error!");
    }


    private boolean isDatabaseConnected() {
        try (Connection ignored = dataSource.getConnection()) {
            return true;
        } catch (SQLException e) {
            //MAY need a better way of handling
            e.printStackTrace();
            return false;
        }
    }
}
