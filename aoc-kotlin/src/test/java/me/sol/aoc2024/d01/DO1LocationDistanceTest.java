package me.sol.aoc2024.d01;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class DO1LocationDistanceTest {
    @Test
    void testSampleDistance() throws IOException {
        try (var stream = getClass().getResourceAsStream("/input/aoc2024/d01-sample")) {
            assertThat(DO1LocationDistance.parse(stream).totalDistance()).isEqualTo(11);
        }
    }

    @Test
    void testSampleSimilarity() throws IOException {
        try (var stream = getClass().getResourceAsStream("/input/aoc2024/d01-sample")) {
            assertThat(DO1LocationDistance.parse(stream).similarity()).isEqualTo(31);
        }
    }
}
