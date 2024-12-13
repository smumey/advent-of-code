package me.sol.aoc2024;

import aoc.Direction;
import me.sol.Answer;
import me.sol.Grid;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Stream;

public class D12GardenGroups {
    private final Grid grid;

    D12GardenGroups(Grid grid) {
        this.grid = grid;
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D12GardenGroups(Utility.readInput(D12GardenGroups.class, Utility::parseCharGrid)));
    }

    private static void clear(int[][] array) {
        Arrays.stream(array).forEach(a -> Arrays.fill(a, -1));
    }

    private int countSides(int[][] lineSegments) {
        int sides = 0;
        for (var segments : lineSegments) {
            int previousLine = -1;
            for (var segment : segments) {
                if (segment != -1) {
                    if (segment != previousLine) {
                        sides += 1;
                    }
                }
                previousLine = segment;
            }
        }
        return sides;
    }

    @Answer
    int p1Cost() {
        var explored = new HashSet<Integer>();
        var cost = 0;
        var coord = 0;
        var queue = new LinkedList<Integer>();
        queue.add(coord);
        explored.add(coord);
        var assigned = new HashSet<Integer>();
        while (!queue.isEmpty()) {
            var regionPerimeter = 0;
            var area = 0;
            var plot = queue.removeFirst();
            if (assigned.contains(plot)) {
                continue;
            }
            var plantType = grid.getValue(plot);
            var regionQueue = new LinkedList<Integer>();
            regionQueue.add(plot);
            var regionExplored = new HashSet<Integer>();
            regionExplored.add(plot);
            while (!regionQueue.isEmpty()) {
                var regionPlot = regionQueue.removeFirst();
                var neighbours = grid.neighbours(regionPlot).toArray();
                var perimeter = 4;
                for (var n : neighbours) {
                    long neighbourType = grid.getValue(n);
                    if (neighbourType == plantType) {
                        perimeter -= 1;
                        if (!regionExplored.contains(n)) {
                            regionQueue.add(n);
                            regionExplored.add(n);
                        }
                    } else {
                        if (!explored.contains(n)) {
                            queue.add(n);
                        }
                    }
                    explored.add(n);
                }
                assigned.add(regionPlot);
                area += 1;
                regionPerimeter += perimeter;
//                System.out.printf("Adding perimeter %d for %d,%d to %c%n", perimeter, grid.getX(regionPlot), grid.getY(regionPlot), (char) plantType);
            }

            cost += regionPerimeter * area;
//            System.out.printf(
//                    "Adding region of type %c, regionPerimeter %d, area %d, cost %d%n",
//                    (char) plantType,
//                    regionPerimeter,
//                    area,
//                    regionPerimeter * area
//            );
        }
        return cost;
    }

    private int[][] makeLineSegmentArray(int positions, int length) {
        return Stream.generate(() -> new int[length])
                .limit(positions)
                .toArray(int[][]::new);
    }

    @Answer
    int p2Cost() {
        var explored = new HashSet<Integer>();
        var cost = 0;
        var coord = 0;
        var queue = new LinkedList<Integer>();
        var verticalLineSegments = makeLineSegmentArray(grid.width() + 1, grid.height());
        var horizontalLineSegments = makeLineSegmentArray(grid.height() + 1, grid.width());
        queue.add(coord);
        explored.add(coord);
        var assigned = new HashSet<Integer>();
        while (!queue.isEmpty()) {
            var area = 0;
            var plot = queue.removeFirst();
            if (assigned.contains(plot)) {
                continue;
            }
            var plantType = grid.getValue(plot);
            var regionQueue = new LinkedList<Integer>();
            regionQueue.add(plot);
            var regionExplored = new HashSet<Integer>();
            regionExplored.add(plot);
            clear(verticalLineSegments);
            clear(horizontalLineSegments);
            while (!regionQueue.isEmpty()) {
                var regionPlot = regionQueue.removeFirst();
                int x = grid.getX(regionPlot);
                int y = grid.getY(regionPlot);
                for (var d : Direction.values()) {
                    int borderPlot = grid.move(regionPlot, d);
                    if (borderPlot == Grid.OUT || grid.getValue(borderPlot) != plantType) {
                        switch (d) {
                            case UP:
                                horizontalLineSegments[y][x] = d.ordinal();
                                break;
                            case RIGHT:
                                verticalLineSegments[x + 1][y] = d.ordinal();
                                break;
                            case DOWN:
                                horizontalLineSegments[y + 1][x] = d.ordinal();
                                break;
                            case LEFT:
                                verticalLineSegments[x][y] = d.ordinal();
                                break;
                        }
                        if (borderPlot != Grid.OUT) {
                            if (!explored.contains(borderPlot)) {
                                queue.add(borderPlot);
                            }
                        }
                    } else {
                        long neighbourType = grid.getValue(borderPlot);
                        if (neighbourType == plantType) {
                            if (!regionExplored.contains(borderPlot)) {
                                regionQueue.add(borderPlot);
                                regionExplored.add(borderPlot);
                            }
                        }
                        explored.add(borderPlot);
                    }
                }

                assigned.add(regionPlot);
                area += 1;
                System.out.printf("Adding %c region at %s%n", (char) plantType, grid.coordToString(regionPlot));
            }

            var regionSides = countSides(verticalLineSegments) + countSides(horizontalLineSegments);
            cost += regionSides * area;
            System.out.printf(
                    "Adding region of type %c, sides %d, area %d, cost %d%n",
                    (char) plantType,
                    regionSides,
                    area,
                    regionSides * area
            );
        }
        return cost;
    }
}
