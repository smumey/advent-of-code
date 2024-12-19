package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D18RamRunTest {

    @Test
    void shortestPath() throws IOException {
        assertThat(new D18RamRun(Utility.readSample(getClass(), D18RamRun::parse), 7, 12).shortestPath()).isEqualTo(22);
    }

    @Test
    void firstBlocking() throws IOException {
        assertThat(new D18RamRun(Utility.readSample(getClass(), D18RamRun::parse), 7, 12).firstBlocking()).isEqualTo("(6,1)");
    }
}
