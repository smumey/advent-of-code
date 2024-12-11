package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class D11PlutonianPebblesTest {

    @Test
    void p1CountPebbles() throws IOException {
        assertThat(new D11PlutonianPebbles(Utility.readSample(getClass(), D11PlutonianPebbles::parse)).p1CountPebbles()).isEqualTo(55312);
    }
}
