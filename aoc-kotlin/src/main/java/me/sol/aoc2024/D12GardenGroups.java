package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Grid;
import me.sol.Utility;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

public class D12GardenGroups {
    private final Grid grid;

    D12GardenGroups(Grid grid) {
        this.grid = grid;
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D12GardenGroups(Utility.readInput(D12GardenGroups.class, Utility::parseCharGrid)));
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
                System.out.printf("Adding perimeter %d for %d,%d to %c%n", perimeter, grid.getX(regionPlot), grid.getY(regionPlot), (char) plantType);
            }

            cost += regionPerimeter * area;
            System.out.printf(
                    "Adding region of type %c, regionPerimeter %d, area %d, cost %d%n",
                    (char) plantType,
                    regionPerimeter,
                    area,
                    regionPerimeter * area
            );
        }
        return cost;
    }
}
