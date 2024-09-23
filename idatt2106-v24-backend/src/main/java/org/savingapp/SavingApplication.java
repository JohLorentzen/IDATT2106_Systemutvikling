package org.savingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Main application class for the SavingApp application.
 * This class is responsible for starting the Spring Boot application.
 * <p>
 * The application is a saving app that allows users to create saving goals,
 * track their progress, and improve their saving habits.
 * </p>
 */
@SpringBootApplication
public class SavingApplication {

    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SavingApplication.class, args);
    }
}