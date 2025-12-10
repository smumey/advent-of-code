package me.sol.aoc2025;

import aoc.Direction;
import me.sol.Answer;
import me.sol.Grid;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;

public record D07Laboratories(Grid grid) {
    public static void main(String[] args) throws IOException {
        Utility.execute(new D07Laboratories(Utility.readInput(D07Laboratories.class, Utility::parseCharGrid)));
    }

    int p1Advance(int step) {
        var splits = 0;
        for (var col = 0; col < grid.width(); col++) {
            var coord = grid.toCoordinate(col, step);
            var cell = grid.getValue(coord);
            if (cell == 'S' || cell == '|') {
//                System.out.printf("step=%d, col=%d, coord=%s%n", step, col, grid.coordToString(coord));
                var nextCoord = grid.move(coord, Direction.DOWN);
                if (grid.getValue(nextCoord) == '^') {
                    splits++;
                    var left = grid.move(nextCoord, Direction.LEFT);
                    if (left != Grid.OUT) {
                        grid.setValue(left, '|');
                    }
                    var right = grid.move(nextCoord, Direction.RIGHT);
                    if (right != Grid.OUT) {
                        grid.setValue(right, '|');
                    }
                } else {
                    grid.setValue(nextCoord, '|');
                }
            }
        }
        return splits;
    }

    long[] p2Advance(int step, long[] counts, long[] nextCounts) {
        Arrays.fill(nextCounts, 0L);
        for (var col = 0; col < grid.width(); col++) {
            var coord = grid.toCoordinate(col, step);
            var cell = grid.getValue(coord);
            if (cell == 'S' || cell == '|') {
//                System.out.printf("step=%d, col=%d, coord=%s%n", step, col, grid.coordToString(coord));
                var nextCoord = grid.move(coord, Direction.DOWN);
                if (grid.getValue(nextCoord) == '^') {
                    var left = grid.move(nextCoord, Direction.LEFT);
                    if (left != Grid.OUT) {
                        grid.setValue(left, '|');
                        nextCounts[grid.getX(left)] += counts[col];
                    }
                    var right = grid.move(nextCoord, Direction.RIGHT);
                    if (right != Grid.OUT) {
                        grid.setValue(right, '|');
                        nextCounts[grid.getX(right)] += counts[col];
                    }
                } else {
                    grid.setValue(nextCoord, '|');
                    nextCounts[grid.getX(nextCoord)] += counts[col];
                }
            }
        }
        return nextCounts;
    }

    @Answer
    int p1SplitCount() {
        var splits = 0;
        for (var step = 0; step < grid.height() - 1; step++) {
            splits += p1Advance(step);
        }
        return splits;
    }

    @Answer
    long p2PossibleCount() {
        var counts = new long[2][grid.width()];
        var startCol = grid.getX(grid.findAll('S')[0]);
        counts[0][startCol] = 1L;
        for (var step = 0; step < grid.height() - 1; step++) {
            p2Advance(step, counts[step % 2], counts[(step + 1) % 2]);
        }
        var total = 0L;
        for (var count : counts[grid.height() % 2]) {
            total += count;
        }
        return total;
    }
}
