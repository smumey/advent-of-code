package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D04WordSearchTest {
    @Test
    void countWords() throws IOException {
        var puzzle = Utility.readSample(this.getClass(), D04WordSearch::parse);
        assertThat(new D04WordSearch(puzzle).countWords()).isEqualTo(17);
    }

    @Test
    void countCrosses() throws IOException {
        var puzzle = Utility.readSample(this.getClass(), D04WordSearch::parse);
        assertThat(new D04WordSearch(puzzle).countCrosses()).isEqualTo(9);
    }
}