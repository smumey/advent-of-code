package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class D22MonkeyMarketTest {

    @Test
    void p1SumSecretNumbers() throws IOException {
        assertThat(new D22MonkeyMarket(Utility.readSample(getClass(), D22MonkeyMarket::parse)).p1SumSecretNumbers())
                .isEqualTo(37327623L);
    }

    @Test
    void p2MaximumBananas() {
        assertThat(new D22MonkeyMarket(List.of(1L, 2L, 3L, 2024L)).p2MaximumBananas()).isEqualTo(23);
    }
}
