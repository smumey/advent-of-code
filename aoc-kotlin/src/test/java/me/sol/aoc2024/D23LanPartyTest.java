package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D23LanPartyTest {

    @Test
    void p1TripletsCount() throws IOException {
        assertThat(new D23LanParty(Utility.readSample(getClass(), D23LanParty::parse)).p1TripletsCount()).isEqualTo(7L);
    }

    @Test
    void p2LargestClique() throws IOException {
        assertThat(new D23LanParty(Utility.readSample(getClass(), D23LanParty::parse)).p2LargestClique())
                .isEqualTo("co,de,ka,ta");
    }
}
