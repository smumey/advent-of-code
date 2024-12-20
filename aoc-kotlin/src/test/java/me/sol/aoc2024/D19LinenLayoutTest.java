package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D19LinenLayoutTest {
    @Test
    void countMatched() throws IOException {
        assertThat(new D19LinenLayout(Utility.readSample(getClass(), D19LinenLayout::parse)).p1CountMatched()).isEqualTo(6);
    }

    @Test
    void countUnique() throws IOException {
        assertThat(new D19LinenLayout(Utility.readSample(getClass(), D19LinenLayout::parse)).p2CountUnique()).isEqualTo(16);
    }
}
