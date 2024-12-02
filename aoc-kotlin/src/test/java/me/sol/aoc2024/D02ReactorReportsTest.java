package me.sol.aoc2024;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

class D02ReactorReportsTest {
    @Test
    void countSafe() throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/input/aoc2024/d02-sample")))) {
            assertThat(new D02ReactorReports(D02ReactorReports.parse(reader)).countSafe()).isEqualTo(2L);
        }
    }

    @Test
    void countSafeDampened() throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/input/aoc2024/d02-sample")))) {
            assertThat(new D02ReactorReports(D02ReactorReports.parse(reader)).countSafeDampened()).isEqualTo(4L);
        }
    }
}