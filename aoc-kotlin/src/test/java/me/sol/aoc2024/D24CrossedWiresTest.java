package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D24CrossedWiresTest {

    @Test
    void p1ZedValue() throws IOException {
        assertThat(new D24CrossedWires(Utility.readSample(getClass(), D24CrossedWires::parse)).p1ZedValue())
                .isEqualTo(2024L);
    }
}
