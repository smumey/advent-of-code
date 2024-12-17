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
import static java.util.stream.Collectors.toUnmodifiableSet;

public class D16ReindeerMaze {
    private final Grid maze;

    public static void main(String[] args) throws IOException {
        Utility.execute(new D16ReindeerMaze(Utility.readInput(D16ReindeerMaze.class, Utility::parseCharGrid)));
    }

    public D16ReindeerMaze(Grid maze) {
        this.maze = maze;
    }

    private Map<Node, Map<Node, Long>> edgeMap(List<Node> nodes) {
        var exits = nodes.stream().filter(n -> maze.getValue(n.pos()) == 'E').collect(toUnmodifiableSet());
        return nodes.stream()
                .map(n ->
                        Map.entry(
                                n,
                                Map.of(
                                        new Node(maze.move(n.pos(), n.direction()), n.direction()), 1L,
                                        new Node(n.pos(), Direction.values()[(n.direction().ordinal() + 1) % 4]), exits.contains(n) ? 0L : 1000L,
                                        new Node(n.pos(), Direction.values()[(n.direction().ordinal() + 3) % 4]), exits.contains(n) ? 0L : 1000L
                                )
                        )
                )
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<Node> nodes() {
        return IntStream.range(0, maze.width() * maze.height())
                .filter(p -> maze.getValue(p) != '#')
                .boxed()
                .flatMap(p -> Arrays.stream(Direction.values()).map(d -> new Node(p, d)))
                .toList();
    }

    @Answer
    long p1LeastCost() {
        var nodes = nodes();
        var source = new Node(Arrays.stream(maze.findAll('S')).findFirst().orElseThrow(), Direction.RIGHT);
        var edgeMap = edgeMap(nodes);
        var pathResponse = Utility.findShortestPath(nodes, n -> edgeMap.get(n), source);
        int exit = Arrays.stream(maze.findAll('E')).findFirst().orElseThrow();
        return pathResponse.distances().applyAsLong(new Node(exit, Direction.RIGHT));
    }

    record Node(int pos, Direction direction) {
    }
}
