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

    @Answer
    long p2AllOptimalPathNodesCount() {
        var nodes = nodes();
        int startPos = Arrays.stream(maze.findAll('S')).findFirst().orElseThrow();
        int endPos = Arrays.stream(maze.findAll('E')).findFirst().orElseThrow();
        var edgeMap = edgeMap(nodes);
        var toPathResponse = Utility.findShortestPath(nodes, n -> edgeMap.get(n), new Node(startPos, Direction.RIGHT));
        var fromPathResponse = Utility.findShortestPath(nodes, n -> edgeMap.get(n), new Node(endPos, Direction.LEFT));
        var minDist = toPathResponse.distances().applyAsLong(new Node(endPos, Direction.RIGHT));
//        System.out.printf("to dist = %d, from dist=%d%n", toPathResponse.distances().applyAsLong(new Node(endPos, Direction.RIGHT)), fromPathResponse.distances().applyAsLong(new Node(startPos, Direction.RIGHT)));
        return nodes.stream()
                .filter(n -> toPathResponse.distances().applyAsLong(n) + fromPathResponse.distances().applyAsLong(new Node(n.pos(), n.direction().opposite())) == minDist)
                .mapToInt(Node::pos)
                .distinct()
                .count();
    }

    record Node(int pos, Direction direction) {
    }
}
