package edu.eci.arep.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes mathematical computation endpoints.
 *
 * @author Jesús Pinzón
 * @version 1.0
 * @since 2026-03-24
 */
@RestController
@RequestMapping("/api/math")
public class MathController {

    /**
     * Computes the sine of a given angle in radians.
     *
     * @param value the angle in radians
     * @return the sine of the given value
     */
    @GetMapping("/sin")
    public double sin(@RequestParam double value) {
        return Math.sin(value);
    }

    /**
     * Computes the cosine of a given angle in radians.
     *
     * @param value the angle in radians
     * @return the cosine of the given value
     */
    @GetMapping("/cos")
    public double cos(@RequestParam double value) {
        return Math.cos(value);
    }

    /**
     * Computes the factorial of a non-negative integer.
     *
     * @param value the non-negative integer
     * @return the factorial of the given value
     * @throws IllegalArgumentException if value is negative or greater than 20
     */
    @GetMapping("/factorial")
    public long factorial(@RequestParam int value) {
        if (value < 0 || value > 20) {
            throw new IllegalArgumentException("Value must be between 0 and 20");
        }
        long result = 1;
        for (int i = 2; i <= value; i++) {
            result *= i;
        }
        return result;
    }
}
