package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D10HoofItTest {

    @Test
    void p1TrailheadScoreSums() throws IOException {
        assertThat(new D10HoofIt(Utility.readSample(getClass(), Utility::parseCharGrid)).p1TrailheadScoreSums())
                .isEqualTo(36);
    }
}
