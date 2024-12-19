package me.sol.aoc2024;

import aoc.Direction;
import me.sol.Answer;
import me.sol.Grid;
import me.sol.Utility;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class D18RamRun {
    private final List<Integer> badBytes;
    private final Grid grid;
    private final int initialLimit;

    public static void main(String[] args) throws IOException {
        Utility.execute(new D18RamRun(Utility.readInput(D18RamRun.class, D18RamRun::parse), 71, 1024));
    }

    static List<List<Integer>> parse(Stream<String> lines) {
        return lines.map(l -> Arrays.stream(l.split(",")).map(Integer::parseInt).toList()).toList();
    }

    D18RamRun(List<List<Integer>> tuples, int size, int initialLimit) {
        badBytes = tuples.stream().map(t -> t.get(0) + size * t.get(1)).toList();
        grid = new Grid(Stream.generate(() -> new long[size]).limit(size).toArray(long[][]::new));
        this.initialLimit = initialLimit;
    }

    @Answer
    long shortestPath() {
        return pathLength(initialLimit);
    }

    private long pathLength(int limit) {
        var badSet = new HashSet<>(badBytes.subList(0, limit));
        var nodes = IntStream.range(0, grid.width() * grid.height()).filter(pos -> !badSet.contains(pos)).boxed().toList();
        var edges = getEdges(nodes, badSet);
        var pathResponse = Utility.findShortestPath(nodes, n -> edges.get(n), 0);
        return pathResponse.distances().applyAsLong(grid.width() * grid.height() - 1);
    }

    @NotNull
    private Map<Integer, Map<Integer, Long>> getEdges(List<Integer> nodes, HashSet<Integer> badSet) {
        return nodes.stream()
                .map(n -> Map.entry(
                        n,
                        Arrays.stream(Direction.values())
                                .map(d -> grid.move(n, d))
                                .filter(m -> m != Grid.OUT && !badSet.contains(m))
                                .collect(toMap(m -> m, m -> 1L))
                ))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Answer
    String firstBlocking() {
        int low = initialLimit;
        int high = badBytes.size();
        while (high - low > 1) {
            int mid = (low + high) / 2;
            var dist = pathLength(mid);
//            System.out.printf("testing mid=%d last=%s dist=%d low=%d high=%d%n", mid, grid.coordToString(badBytes.get(mid - 1)), dist, high, mid);
            if (dist < Long.MAX_VALUE) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return grid.coordToString(badBytes.get(high - 1));
    }
}
