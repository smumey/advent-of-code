package me.sol.aoc2025;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class D08PlaygroundTest {
    @Test
    void p1LargestProduct() throws IOException {
        assertThat(new D08Playground(Utility.readSample(getClass(), D08Playground::parse), 10).p1LargestProduct()).isEqualTo(40L);
    }

    void p2XCoordProduct() throws IOException {
        assertThat(new D08Playground(Utility.readSample(getClass(), D08Playground::parse), 10).p2XCoordProduct()).isEqualTo(25272L);
    }
}
