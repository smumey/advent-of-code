package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D06GuardGallivantTest {

    @Test
    void distinctGuardPositionCount() throws IOException {
        assertThat(new D06GuardGallivant(Utility.readSample(D06GuardGallivant.class, D06GuardGallivant::parse)).distinctGuardPositionCount())
                .isEqualTo(41);
    }

    @Test
    void guardLoopBlockCount() throws IOException {
        assertThat(new D06GuardGallivant(Utility.readSample(D06GuardGallivant.class, D06GuardGallivant::parse)).guardLoopBlockCount())
                .isEqualTo(6);
    }
}