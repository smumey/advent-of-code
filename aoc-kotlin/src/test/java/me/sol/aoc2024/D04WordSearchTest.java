package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

class D04WordSearchTest {
    @Test
    void count() throws IOException {
        var puzzle = Utility.readSample(this.getClass(), D04WordSearch::parse);
        System.out.println(Arrays.toString(puzzle.lines()));
        System.out.println(puzzle.diagonals(false).toList());
    }
}