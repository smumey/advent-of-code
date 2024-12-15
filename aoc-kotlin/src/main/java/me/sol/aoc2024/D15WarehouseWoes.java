package me.sol.aoc2024;

import aoc.Direction;
import me.sol.Answer;
import me.sol.Grid;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static aoc.Direction.DOWN;
import static aoc.Direction.LEFT;
import static aoc.Direction.RIGHT;
import static aoc.Direction.UP;

public class D15WarehouseWoes {
    private static final long[] WIDE_BOX = {'[', ']'};
    private static final long[] WIDE_ROBOT = {'@', '.'};
    private static final long[] WIDE_SPACE = {'.', '.'};
    private static final long[] WIDE_WALL = {'#', '#'};
    private final State initialState;

    public static void main(String[] args) throws IOException {
        Utility.execute(new D15WarehouseWoes(Utility.readInput(D15WarehouseWoes.class, D15WarehouseWoes::parse)));
    }

    static State parse(Stream<String> lineStream) {
        var lines = lineStream.toList();
        int sepLineIndex = lines.indexOf("");
        var grid = Utility.parseCharGrid(lines.subList(0, sepLineIndex).stream());
        var directions = lines.subList(sepLineIndex + 1, lines.size()).stream()
                .map(String::trim).flatMapToInt(String::chars).mapToObj(c -> switch (c) {
                    case '^' -> UP;
                    case '>' -> RIGHT;
                    case 'v' -> DOWN;
                    case '<' -> LEFT;
                    default -> throw new IllegalArgumentException("Cannot convert %c to direction".formatted(c));
                }).toList();
        var robotPos = grid.findAll('@')[0];
        return new State(grid, robotPos, directions, 0);
    }

    private static long gpsSum(State finalState, char value) {
        return Arrays.stream(finalState.grid().findAll(value))
                .mapToLong(p -> 100L * finalState.grid().getY(p) + finalState.grid().getX(p))
                .sum();
    }

    public D15WarehouseWoes(State initialState) {
        this.initialState = initialState;
    }

    @Answer
    long p1SumBoxGps() {
        var finalState = Stream.iterate(initialState, s -> s != null, s -> s.nextState())
                .reduce((s1, s2) -> s2).orElseThrow();

        return gpsSum(finalState, 'O');
    }

    private long[] mapToWide(int cell) {
        return switch (cell) {
            case 'O' -> WIDE_BOX;
            case '@' -> WIDE_ROBOT;
            case '.' -> WIDE_SPACE;
            case '#' -> WIDE_WALL;
            default -> throw new IllegalArgumentException();
        };
    }

    @Answer
    long p2SumDoubleWideBoxGps() {
        var wideArray = Arrays.stream(initialState.grid().getRows())
                .map(r -> Arrays.stream(r).flatMap(cell -> Arrays.stream(mapToWide((int) cell))).toArray())
                .toArray(long[][]::new);
        var wideGrid = new Grid(wideArray);
        var state = new State(
                wideGrid,
                Arrays.stream(wideGrid.findAll('@')).findFirst().orElseThrow(),
                initialState.moves(),
                0
        );
        var finalState = Stream.iterate(state, s -> s != null, s -> s.nextStateWide())
                .reduce((s1, s2) -> s2).orElseThrow();
        return gpsSum(finalState, '[');
    }

    record State(Grid grid, int robotPos, List<Direction> moves, int iteration) {
        State nextState() {
//            grid.printAsChars(System.out);
            if (iteration >= moves.size()) {
                return null;
            }
//            System.out.println(moves.get(0));
            var nextGrid = grid.copy();
            var direction = moves.get(iteration);
            var opposite = direction.opposite();
            var nextPos = grid.move(robotPos, direction);
            var firstEmpty = IntStream.iterate(nextPos, c -> nextGrid.getValue(c) != '#', p -> nextGrid.move(p, direction))
                    .filter(p -> nextGrid.getValue(p) == '.')
                    .boxed()
                    .findFirst();
            return firstEmpty.map(empty -> {
                IntStream.iterate(empty, p -> p != robotPos, p -> nextGrid.move(p, opposite))
                        .forEach(p -> nextGrid.setValue(p, nextGrid.getValue(nextGrid.move(p, opposite))));
                nextGrid.setValue(robotPos, '.');
                return new State(nextGrid, nextPos, moves, iteration + 1);

            }).orElse(new State(grid, robotPos, moves, iteration + 1));
        }

        State nextStateWide() {
//            grid.printAsChars(System.out);
            if (iteration >= moves.size()) {
                return null;
            }
            var nextGrid = grid.copy();
            var direction = moves.get(iteration);
//            System.out.printf("iteration: %d direction: %s%n", iteration, direction);
            var toMove = new LinkedList<Integer>();
            var queue = new LinkedList<Integer>();
            var explored = new HashSet<Integer>();
            var enqueue = (Consumer<Integer>) p -> {
                if (!explored.contains(p)) {
                    queue.add(p);
                    explored.add(p);
                }
            };
            enqueue.accept(robotPos);
            while (!queue.isEmpty()) {
                int pos = queue.removeFirst();
                long cell = grid.getValue(pos);
//                System.out.printf("processing %c at %s%n", (char) cell, nextGrid.coordToString(pos));
                int nextPos = nextGrid.move(pos, direction);
                if (cell == '#') {
                    return new State(nextGrid, robotPos, moves, iteration + 1);
                } else if (cell == '.') {
                    continue;
                } else if (direction == UP || direction == DOWN) {
                    int neighbour;
                    if (cell == '[') {
                        enqueue.accept(nextGrid.move(pos, RIGHT));
                    } else if (cell == ']') {
                        enqueue.accept(nextGrid.move(pos, LEFT));
                    }
                }
                enqueue.accept(nextPos);
                toMove.add(pos);
            }
            for (int pos : toMove.reversed()) {
                int nextPos = nextGrid.move(pos, direction);
//                System.out.printf("Moving %c at %s to %s%n", (char) nextGrid.getValue(pos), grid.coordToString(pos), grid.coordToString(nextPos));
                nextGrid.setValue(nextPos, nextGrid.getValue(pos));
                nextGrid.setValue(pos, '.');
            }
            return new State(nextGrid, nextGrid.move(robotPos, direction), moves, iteration + 1);
        }
    }
}
