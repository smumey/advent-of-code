package me.sol.aoc2025;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class D07LaboratoriesTest {
    @Test
    void p1SplitCount() throws IOException {
        assertThat(new D07Laboratories(Utility.readSample(getClass(), Utility::parseCharGrid)).p1SplitCount()).isEqualTo(21);
    }

    @Test
    void p2SplitCount() throws IOException {
        assertThat(new D07Laboratories(Utility.readSample(getClass(), Utility::parseCharGrid)).p2PossibleCount()).isEqualTo(40L);
    }
}
