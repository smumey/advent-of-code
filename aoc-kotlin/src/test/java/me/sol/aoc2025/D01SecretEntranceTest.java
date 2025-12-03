package me.sol.aoc2025;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class D01SecretEntranceTest {
    @Test
    void p1ZeroCount() throws IOException {
        assertThat(new D01SecretEntrance(
                D01SecretEntrance.START_POS,
                D01SecretEntrance.CLICKS,
                Utility.readSample(getClass(), D01SecretEntrance::parse)
        ).zeroCount()).isEqualTo(3L);
    }

    @Test
    void p2ZeroCount() throws IOException {
        assertThat(new D01SecretEntrance(
                D01SecretEntrance.START_POS,
                D01SecretEntrance.CLICKS,
                Utility.readSample(getClass(), D01SecretEntrance::parse)
        ).zeroClickCount()).isEqualTo(6L);
    }
}
