package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D03CorruptMultTest {

    @Test
    void sumProducts() throws IOException {
        assertThat(new D03CorruptMult(Utility.readSample(getClass(), D03CorruptMult::parse)).sumProducts()).isEqualTo(161);
    }
}