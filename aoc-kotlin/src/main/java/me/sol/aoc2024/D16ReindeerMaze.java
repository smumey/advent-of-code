package me.sol.aoc2024;

import me.sol.Grid;
import me.sol.Utility;

import java.io.IOException;

public class D16ReindeerMaze {
    private final Grid maze;

    public static void main(String[] args) throws IOException {
        Utility.execute(new D16ReindeerMaze(Utility.readInput(D16ReindeerMaze.class, Utility::parseCharGrid)));
    }

    public D16ReindeerMaze(Grid maze) {
        this.maze = maze;
    }

    @Answer
    long p1LeastCost() {

        return maze.dijkstra(0, 0, maze.width() - 1, maze.height() - 1);
    }
}
