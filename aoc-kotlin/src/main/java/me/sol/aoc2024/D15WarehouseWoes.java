package me.sol.aoc2024;

import aoc.Direction;
import me.sol.Answer;
import me.sol.Grid;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static aoc.Direction.DOWN;
import static aoc.Direction.LEFT;
import static aoc.Direction.RIGHT;
import static aoc.Direction.UP;

public class D15WarehouseWoes {
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
        return new State(grid, robotPos, directions);
    }

    public D15WarehouseWoes(State initialState) {
        this.initialState = initialState;
    }

    @Answer
    long sumBoxGps() {
        var finalState = Stream.iterate(initialState, s -> s != null, s -> s.nextState())
                .reduce((s1, s2) -> s2).orElseThrow();

        return Arrays.stream(finalState.grid().findAll('O'))
                .mapToLong(p -> 100L * finalState.grid().getY(p) + finalState.grid().getX(p))
                .sum();
    }

    record State(Grid grid, int robotPos, List<Direction> moves) {
        State nextState() {
//            grid.printAsChars(System.out);
            if (moves.isEmpty()) {
                return null;
            }
//            System.out.println(moves.get(0));
            var nextGrid = grid.copy();
            var direction = moves.get(0);
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
                return new State(nextGrid, nextPos, moves.subList(1, moves.size()));

            }).orElse(new State(grid, robotPos, moves.subList(1, moves.size())));
        }
    }
}
