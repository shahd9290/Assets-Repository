package com.example.assetsystembackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class to run the Asset System Backend application.
 */
@SpringBootApplication
public class AssetSystemBackendApplication {

    /**
     * Start the backend server.
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(AssetSystemBackendApplication.class, args);
    }

}