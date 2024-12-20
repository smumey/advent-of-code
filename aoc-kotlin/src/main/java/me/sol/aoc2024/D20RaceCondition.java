package me.sol.aoc2024;

import aoc.Direction;
import me.sol.Answer;
import me.sol.Grid;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public class D20RaceCondition {
    private final Grid grid;
    private final int minSavedTime;

    public static void main(String[] args) throws IOException {
        Utility.execute(new D20RaceCondition(Utility.readInput(D20RaceCondition.class, Utility::parseCharGrid), 100));
    }

    D20RaceCondition(Grid grid, int minSavedTime) {
        this.grid = grid;
        this.minSavedTime = minSavedTime;
    }

    List<Integer> getNodes() {
        return IntStream.range(0, grid.width() * grid.height())
                .filter(pos -> grid.getValue(pos) != '#')
                .boxed()
                .toList();
    }

    Map<Integer, Map<Integer, Long>> edges(List<Integer> nodes) {
        return nodes.stream()
                .map(pos -> Map.entry(
                        pos,
                        Arrays.stream(Direction.values())
                                .map(d -> grid.move(pos, d))
                                .filter(p -> p != Grid.OUT && grid.getValue(p) != 'p')
                                .collect(toMap(p -> p, p -> 1L))
                ))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Answer
    long p1NumberOfCheats() {
        var nodes = getNodes();
        var edges = edges(nodes);
        var start = grid.findAll('S')[0];
        var finish = grid.findAll('E')[0];
        var pathResponse = Utility.findShortestPath(nodes, n -> edges.get(n), start);
        var path = IntStream.iterate(finish, n -> n != -1, n -> pathResponse.previous().apply(n).orElse(-1))
                .boxed()
                .toList()
                .reversed();
        var stepMap = IntStream.range(0, path.size()).boxed().collect(toMap(i -> path.get(i), i -> i));
        return path.subList(0, path.size() - minSavedTime).stream()
                .flatMap(
                        pos -> Arrays.stream(Direction.values())
                                .flatMap(d1 -> Arrays.stream(Direction.values()).map(d2 -> new Cheat(pos, grid.move(grid.move(pos, d1), d2))))
                )
                .filter(cheat -> cheat.to() != Grid.OUT && stepMap.computeIfAbsent(cheat.to(), q -> 0) - stepMap.get(cheat.from) - 2 >= minSavedTime)
                .distinct()
                .count();

    }

    @Answer
    long p2NumberOfCheats() {
        var nodes = getNodes();
        var edges = edges(nodes);
        var start = grid.findAll('S')[0];
        var finish = grid.findAll('E')[0];
        var pathResponse = Utility.findShortestPath(nodes, n -> edges.get(n), start);
        var path = IntStream.iterate(finish, n -> n != -1, n -> pathResponse.previous().apply(n).orElse(-1))
                .boxed()
                .toList()
                .reversed();
        var stepMap = IntStream.range(0, path.size()).boxed().collect(toMap(i -> path.get(i), i -> i));
        return IntStream.range(0, path.size() - minSavedTime)
                .boxed()
                .flatMap(startIndex -> IntStream.range(startIndex + minSavedTime, path.size()).mapToObj(endIndex -> new Cheat(path.get(startIndex), path.get(endIndex))))
                .filter(cheat -> {
                    var dist = grid.distance(cheat.to(), cheat.from());
                    return dist <= 20 && stepMap.get(cheat.to()) - stepMap.get(cheat.from()) - dist >= minSavedTime;
                })
                .count();
    }

    record Cheat(int from, int to) {
    }
}
