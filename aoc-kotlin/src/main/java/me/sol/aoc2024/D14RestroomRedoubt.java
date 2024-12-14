package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.signum;
import static java.util.function.Predicate.not;

public class D14RestroomRedoubt {
    private final Robot[] robots;
    private final int width;
    private final int height;

    static Robot[] parse(Stream<String> lines) {
        return lines
                .filter(not(String::isEmpty))
                .map(l -> {
                    int[] numbers = Arrays.stream(l.split("[^\\-0-9]+")).filter(not(String::isEmpty)).mapToInt(Integer::parseInt).toArray();
                    return new Robot(numbers[0], numbers[1], numbers[2], numbers[3]);
                }).toArray(Robot[]::new);
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D14RestroomRedoubt(Utility.readInput(D14RestroomRedoubt.class, D14RestroomRedoubt::parse), 101, 103));
    }

    D14RestroomRedoubt(Robot[] robots, int width, int height) {
        this.robots = robots;
        this.width = width;
        this.height = height;
    }

    @Answer
    int p1SafetyFactor() {
        return Arrays.stream(robots)
                .map(r -> r.move(100, width, height))
                .collect(Collectors.groupingBy(g -> signum(g.x() - (width - 1) / 2) + 1 + 3 * (signum((g.y() - (height - 1) / 2)) + 1)))
                .entrySet().stream()
//                .peek(System.out::println)
                .filter(e -> e.getKey() / 3 != 1 && e.getKey() % 3 != 1)
                .map(Map.Entry::getValue)
                .mapToInt(List::size)
                .reduce(1, (x, y) -> x * y);
    }

    private void clear(char[][] grid) {
        Arrays.stream(grid).forEach(row -> Arrays.fill(row, '.'));
    }

    private void print(char[][] grid) {
        Arrays.stream(grid).forEach(System.out::println);
    }

    @Answer
    int p2ChristmasTree() {
        var grid = Stream.generate(() -> new char[width]).limit(height).toArray(char[][]::new);

        var moved = robots.clone();
        for (int t = 1; true; t++) {
            for (int i = 0; i < robots.length; i++) {
                moved[i] = robots[i].move(t, width, height);
            }
            if (Arrays.stream(moved).mapToInt(r -> r.y() * width + r.x()).distinct().count() == robots.length) {
                System.out.printf("Grid at time %d s%n", t);
                clear(grid);
                Arrays.stream(moved).forEach(r -> grid[r.y()][r.x()] = '*');
                print(grid);
                return t;
            }
        }
    }

    record Robot(int x, int y, int velX, int velY) {
        Robot move(int seconds, int width, int height) {
            return new Robot(
                    ((x + velX * seconds) % width + width) % width,
                    ((y + velY * seconds) % height + height) % height,
                    velX,
                    velY
            );
        }
    }
}
