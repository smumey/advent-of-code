package me.sol.aoc2025;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class D05CafeteriaTest {
    @Test
    void p1FreshCount() throws IOException {
        assertThat(Utility.readSample(getClass(), D05Cafeteria::parse).p1FreshCount()).isEqualTo(3);
    }

    @Test
    void p2AllFreshCount() throws IOException {
        assertThat(Utility.readSample(getClass(), D05Cafeteria::parse).p2AllFreshCount()).isEqualTo(14L);
    }
}
