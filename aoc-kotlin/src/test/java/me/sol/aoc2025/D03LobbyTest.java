package me.sol.aoc2025;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class D03LobbyTest {

    @Test
    void p1MaxVoltageSum() throws IOException {
        assertThat(new D03Lobby(Utility.readSample(getClass(), D03Lobby::parse)).p1MaxVoltageSum()).isEqualTo(357);
    }

    @Test
    void p2MaxVoltageSum() throws IOException {
        assertThat(new D03Lobby(Utility.readSample(getClass(), D03Lobby::parse)).p2MaxVoltageSum()).isEqualTo(3121910778619L);
    }
}
