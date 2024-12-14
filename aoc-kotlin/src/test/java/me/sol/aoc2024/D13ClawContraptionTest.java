package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D13ClawContraptionTest {

    @Test
    void p1TokenCount() throws IOException {
        assertThat(new D13ClawContraption(Utility.readSample(getClass(), D13ClawContraption::parse)).p1TokenCount()).isEqualTo(480L);
    }

    @Test
    void p2TokenCount() throws IOException {
        assertThat(new D13ClawContraption(Utility.readSample(getClass(), D13ClawContraption::parse)).p2TokenCount()).isEqualTo(0L);
    }
}
