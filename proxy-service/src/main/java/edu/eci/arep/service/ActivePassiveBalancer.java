package edu.eci.arep.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Service that implements an active-passive load balancing algorithm
 * across two Math Service instances. Requests are always sent to the
 * active instance; if it fails, the passive instance is promoted.
 *
 * @author Jesús Pinzón
 * @version 1.0
 * @since 2026-03-24
 */
@Service
public class ActivePassiveBalancer {

    private final String[] instances;
    private int activeIndex;

    /**
     * Initializes the balancer reading instance URLs from environment variables
     * MATH_SERVICE_1 and MATH_SERVICE_2. Falls back to localhost defaults.
     */
    public ActivePassiveBalancer() {
        String instance1 = System.getenv("MATH_SERVICE_1") != null
                ? System.getenv("MATH_SERVICE_1")
                : "http://localhost:8081";
        String instance2 = System.getenv("MATH_SERVICE_2") != null
                ? System.getenv("MATH_SERVICE_2")
                : "http://localhost:8082";
        this.instances = new String[]{instance1, instance2};
        this.activeIndex = 0;
    }

    /**
     * Forwards a request to the active Math Service instance.
     * If the active instance is unavailable, switches to the passive one.
     *
     * @param path the API path to invoke (e.g. /api/math/sin?value=1.0)
     * @return the response body from the Math Service
     * @throws RuntimeException if both instances are unavailable
     */
    public String forward(String path) {
        for (int attempt = 0; attempt < instances.length; attempt++) {
            String baseUrl = instances[activeIndex];
            try {
                return get(baseUrl + path);
            } catch (Exception e) {
                activeIndex = (activeIndex + 1) % instances.length;
            }
        }
        throw new RuntimeException("All Math Service instances are unavailable");
    }

    /**
     * Executes an HTTP GET request and returns the response body.
     *
     * @param urlString the full URL to request
     * @return the response body as a String
     * @throws Exception if the connection fails or returns a non-200 status
     */
    private String get(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(3000);
        con.setReadTimeout(3000);
        con.setRequestProperty("Accept", "application/json");

        if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new Exception("Non-200 response from " + urlString);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        return response.toString();
    }
}
