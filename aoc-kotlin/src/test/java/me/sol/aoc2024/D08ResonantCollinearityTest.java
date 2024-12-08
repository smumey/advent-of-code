package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D08ResonantCollinearityTest {

    @Test
    void p1CountAntinodes() throws IOException {
        assertThat(new D08ResonantCollinearity(Utility.readSample(D08ResonantCollinearityTest.class, Utility::parseCharGrid)).p1CountAntinodes())
                .isEqualTo(14);
    }
}
