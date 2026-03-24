package edu.eci.arep.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * REST controller that exposes mathematical computation endpoints.
 *
 * @author Jesús Pinzón
 * @version 1.0
 * @since 2026-03-24
 */
@RestController
public class MathController {

    /**
     * Computes the Catalan sequence from C0 to Cn using dynamic programming
     * based on the recurrence relation: Cn = sum(Ci * C(n-1-i)) for i=0..n-1.
     *
     * @param value the non-negative integer n
     * @return a map with operation name, input, and output sequence as JSON
     * @throws IllegalArgumentException if value is negative
     */
    @GetMapping("/catalan")
    public Map<String, Object> catalan(@RequestParam int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be a non-negative integer");
        }

        BigInteger[] c = new BigInteger[value + 1];
        c[0] = BigInteger.ONE;

        for (int n = 1; n <= value; n++) {
            c[n] = BigInteger.ZERO;
            for (int i = 0; i < n; i++) {
                c[n] = c[n].add(c[i].multiply(c[n - 1 - i]));
            }
        }

        StringJoiner joiner = new StringJoiner(", ");
        for (BigInteger val : c) {
            joiner.add(val.toString());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("operation", "Catalan Sequence");
        response.put("input", value);
        response.put("output", joiner.toString());
        return response;
    }
}
