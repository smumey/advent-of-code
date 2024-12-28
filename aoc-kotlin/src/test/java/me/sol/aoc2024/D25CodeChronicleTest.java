package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D25CodeChronicleTest {

    @Test
    void p1NumMatchingPairs() throws IOException {
        assertThat(new D25CodeChronicle(Utility.readSample(getClass(), D25CodeChronicle::parse)).p1NumMatchingPairs())
                .isEqualTo(3L);
    }
}
