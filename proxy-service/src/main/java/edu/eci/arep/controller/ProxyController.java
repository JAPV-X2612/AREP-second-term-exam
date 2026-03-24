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
@RequestMapping("/proxy")
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
     * Proxies a Catalan sequence computation request to the active Math Service.
     *
     * @param value the non-negative integer n
     * @return the JSON response from the Math Service or a 503 error
     */
    @GetMapping("/catalan")
    public ResponseEntity<String> catalan(@RequestParam int value) {
        return forward("/catalan?value=" + value);
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
