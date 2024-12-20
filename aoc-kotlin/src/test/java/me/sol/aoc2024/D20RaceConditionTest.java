package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D20RaceConditionTest {

    @Test
    void numberOfCheats() throws IOException {
        assertThat(new D20RaceCondition(Utility.readSample(getClass(), Utility::parseCharGrid), 20).numberOfCheats()).isEqualTo(5);
    }
}
