package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D07BridgeRepairTest {

    @Test
    void p1CalibrationSum() throws IOException {
        assertThat(new D07BridgeRepair(Utility.readSample(this.getClass(), D07BridgeRepair::parse)).p1CalibrationSum())
                .isEqualTo(3749L);
    }

    @Test
    void p2CalibrationSum() throws IOException {
        assertThat(new D07BridgeRepair(Utility.readSample(this.getClass(), D07BridgeRepair::parse)).p2CalibrationSum())
                .isEqualTo(11387L);
    }
}