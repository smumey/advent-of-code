package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Grid;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class D18RamRun {
    private final List<Integer> badBytes;
    private final Grid grid;

    List<List<Integer>> parse(Stream<String> lines) {
        return lines.map(l -> Arrays.stream(l.split(",")).map(Integer::parseInt).toList()).toList();
    }

    D18RamRun(List<List<Integer>> tuples, int size) {
        badBytes = tuples.stream().map(t -> t.get(0) + size * t.get(1)).toList();
        grid = new Grid(Stream.generate(() -> new long[size]).limit(size).toArray(long[][]::new));
    }

    @Answer
    int shortestPath() {
        var nodes = IntStream.range(0, grid.width()*grid.height()).filter(pos -> !badBytes.contains(pos)).boxed().toList();
        var edges = nodes.stream().flatmap();
        var pathResponse = Utility.
    }
}
