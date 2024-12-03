package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D01LocationDistanceTest {
    @Test
    void testSampleDistance() throws IOException {
        assertThat(Utility.readSample(this.getClass(), D01LocationDistance::parse).totalDistance()).isEqualTo(11);
    }

    @Test
    void testSampleSimilarity() throws IOException {
        assertThat(Utility.readSample(this.getClass(), D01LocationDistance::parse).similarity()).isEqualTo(31);
    }
}
