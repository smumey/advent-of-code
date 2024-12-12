package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D12GardenGroupsTest {

    @Test
    void p1Cost() throws IOException {
        assertThat(new D12GardenGroups(Utility.readSample(getClass(), Utility::parseCharGrid)).p1Cost()).isEqualTo(1930);
    }
}
