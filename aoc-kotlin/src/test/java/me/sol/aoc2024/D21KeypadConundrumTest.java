package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class D21KeypadConundrumTest {

    @Test
    void complexitySum() throws IOException {
        assertThat(new D21KeypadConundrum(Utility.readSample(getClass(), D21KeypadConundrum::parse)).complexitySum()).isEqualTo(126384);
    }
}
