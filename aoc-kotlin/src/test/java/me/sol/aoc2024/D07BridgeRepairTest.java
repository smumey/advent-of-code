package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class D07BridgeRepairTest {

    @Test
    void calibrationSum() throws IOException {
        assertThat(new D07BridgeRepair(Utility.readSample(this.getClass(), D07BridgeRepair::parse)).calibrationSum())
                .isEqualTo(3749L);
    }
}