package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

class D02ReactorReportsTest {
    @Test
    void countSafe() throws IOException {
        assertThat(new D02ReactorReports(Utility.readSample(getClass(), D02ReactorReports::parse)).countSafe()).isEqualTo(2L);
    }

    @Test
    void countSafeDampened() throws IOException {
        var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/input/aoc2024/d02-sample")));
        assertThat(new D02ReactorReports(Utility.readSample(getClass(), D02ReactorReports::parse)).countSafeDampened()).isEqualTo(4L);
    }
}