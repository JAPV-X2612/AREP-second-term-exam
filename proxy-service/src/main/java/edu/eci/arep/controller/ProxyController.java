package edu.eci.arep.controller;

import edu.eci.arep.service.ActivePassiveBalancer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that delegates mathematical computation requests
 * to the active Math Service instance via the active-passive balancer.
 *
 * @author Jesús Pinzón
 * @version 1.0
 * @since 2026-03-24
 */
@RestController
@RequestMapping("/proxy/math")
public class ProxyController {

    private final ActivePassiveBalancer balancer;

    /**
     * Constructs the controller with its required balancer.
     * @param balancer the active-passive load balancer
     */
    public ProxyController(ActivePassiveBalancer balancer) {
        this.balancer = balancer;
    }

    /**
     * Proxies a sine computation request to the active Math Service.
     *
     * @param value the angle in radians
     * @return the sine result or an error message
     */
    @GetMapping("/sin")
    public ResponseEntity<String> sin(@RequestParam double value) {
        return forward("/api/math/sin?value=" + value);
    }

    /**
     * Proxies a cosine computation request to the active Math Service.
     *
     * @param value the angle in radians
     * @return the cosine result or an error message
     */
    @GetMapping("/cos")
    public ResponseEntity<String> cos(@RequestParam double value) {
        return forward("/api/math/cos?value=" + value);
    }

    /**
     * Proxies a factorial computation request to the active Math Service.
     *
     * @param value the non-negative integer
     * @return the factorial result or an error message
     */
    @GetMapping("/factorial")
    public ResponseEntity<String> factorial(@RequestParam int value) {
        return forward("/api/math/factorial?value=" + value);
    }

    /**
     * Forwards a path to the balancer and wraps the result in a ResponseEntity.
     *
     * @param path the API path with query parameters
     * @return 200 OK with the result, or 503 if all instances are down
     */
    private ResponseEntity<String> forward(String path) {
        try {
            return ResponseEntity.ok(balancer.forward(path));
        } catch (RuntimeException e) {
            return ResponseEntity.status(503).body("Service unavailable: " + e.getMessage());
        }
    }
}
