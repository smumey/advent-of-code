package me.sol.aoc2025;

import me.sol.Answer;
import me.sol.Grid;
import me.sol.Utility;

import java.io.IOException;

public record D04PrintingDepartment(Grid grid) {
    public static void main(String[] args) throws IOException {
        Utility.execute(
                new D04PrintingDepartment(Utility.readInput(D04PrintingDepartment.class, Utility::parseCharGrid))
        );
    }

    @Answer
    public int p1RollCount() {
        var count = 0;
        for (var cell : grid().findAll('@')) {
            if (grid.getSurroundingCount(cell, '@') < 4) {
                count++;
            }
        }
        return count;
    }

    @Answer
    int p2RemoveCount() {
        var count = 0;
        var stepCount = 0;
        do {
            stepCount = 0;
            for (var cell : grid().findAll('@')) {
                if (grid.getSurroundingCount(cell, '@') < 4) {
                    stepCount++;
                    grid.setValue(cell, '.');
                }
            }
            count += stepCount;
        }
        while (stepCount > 0);
        return count;
    }
}
