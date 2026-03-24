package edu.eci.arep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Proxy Service application.
 *
 * @author Jesús Pinzón
 * @version 1.0
 * @since 2026-03-24
 */
@SpringBootApplication
public class ProxyApplication {

    /**
     * Launches the Proxy Service Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ProxyApplication.class, args);
    }
}
