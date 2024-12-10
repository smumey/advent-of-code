package me.sol.aoc2024;

import aoc.Direction;
import me.sol.Answer;
import me.sol.Grid;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class D10HoofIt {
    private final Grid grid;

    public D10HoofIt(Grid grid) {
        this.grid = grid;
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D10HoofIt(Utility.readInput(D10HoofIt.class, Utility::parseCharGrid)));
    }

    private IntStream neighbours(int coord) {
        var value = grid.getValue(coord);
        return Arrays.stream(Direction.values())
                .mapToInt(d -> grid.move(coord, d))
                .filter(n -> n != Grid.OUT && grid.getValue(n) == value - 1);
    }

    @Answer
    long p1TrailheadScoreSums() {
        var peaks = grid.findAll('9');
        var trailheadCounts = 0; // Arrays.stream(grid.findAll('0')).boxed().collect(toMap(Function.identity(), v -> 0));
        for (var peak : peaks) {
            var queue = new LinkedList<Integer>();
            var seen = new HashSet<Integer>();
            queue.add(peak);
            while (!queue.isEmpty()) {
                var node = queue.removeLast();
                System.out.printf("node y=%d x=%d value=%c%n", grid.getY(node), grid.getX(node), (char) grid.getValue(node));
                seen.add(node);
                if (grid.getValue(node) == '0') {
                    trailheadCounts += 1;
                } else {
                    neighbours(node).filter(c -> !seen.contains(c)).forEach(queue::add);
                }
            }
        }
        return trailheadCounts;
    }
}
