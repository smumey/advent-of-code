package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D16ReindeerMazeTest {

    @Test
    void p1LeastCost() throws IOException {
        assertThat(new D16ReindeerMaze(Utility.readSample(getClass(), Utility::parseCharGrid)).p1LeastCost()).isEqualTo(7036L);
    }
}
