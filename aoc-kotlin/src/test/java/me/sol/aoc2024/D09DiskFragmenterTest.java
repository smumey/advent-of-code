package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D09DiskFragmenterTest {

    @Test
    void p1Checksum() throws IOException {
        assertThat(new D09DiskFragmenter(Utility.readSample(getClass(), D09DiskFragmenter::parse)).p1Checksum()).isEqualTo(1928L);
    }
}
