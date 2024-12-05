package me.sol.aoc2024;

import aoc.Coordinate;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class D04WordSearch {
    private static final String search = "XMAS";
    private static final String reversedSearch = "SAMX";
    private final Puzzle puzzle;

    D04WordSearch(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    static Puzzle parse(Stream<String> lines) {
        return new Puzzle(lines.toArray(String[]::new));
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new D04WordSearch(Utility.readInput(D04WordSearch.class, D04WordSearch::parse)).countWords());
    }

    int countMatches(Pattern pattern) {
        return Stream.of(puzzle.horizontals(), puzzle.verticals(), puzzle.diagonals(false), puzzle.diagonals(true))
                .flatMap(Function.identity())
                .mapToInt(line -> {
                    var m = pattern.matcher(line);
                    var count = 0;
                    while (m.find()) {
                        count += 1;
                    }
                    System.out.printf("pattern=%s, line=%s count=%d%n", pattern, line, count);
                    return count;
                })
                .sum();
    }

    int countWords() {
        var forward = Pattern.compile(search);
        var reverse = Pattern.compile(reversedSearch);
        return countMatches(Pattern.compile(search)) + countMatches(Pattern.compile(reversedSearch));
    }
}

record Puzzle(String[] lines) {
    int size() {
        return lines.length;
    }

    Stream<String> horizontals() {
        return Arrays.stream(lines);
    }

    Stream<String> verticals() {
        return IntStream.range(0, size())
                .mapToObj(
                        j -> IntStream.range(0, size())
                                .mapToObj(i -> lines[i].charAt(j))
                                .collect(StringBuilder::new, StringBuilder::append, (b1, b2) -> b1.append(b2))
                                .toString()

                );
    }

    private boolean inbounds(Coordinate coordinate) {
        int n = size();
        int x = coordinate.getX();
        int y = coordinate.getY();
        return 0 <= x && x < n && 0 <= y && y < n;
    }

    Stream<String> diagonals(boolean up) {
        var n = size();
        return IntStream.range(0, 2 * n - 1)
                .mapToObj(
                        yOffset -> {
                            return IntStream.range(0, n)
                                    .mapToObj(x -> {
                                        var y = up ? -n + 1 + yOffset + x : 2 * n - 2 - yOffset - x;
                                        var coordinate = new Coordinate(x, y);
//                                        System.out.printf(
//                                                "up=%b, yOffset=%d, x=%d, coordinate=%s%n",
//                                                up,
//                                                yOffset,
//                                                x,
//                                                coordinate
//                                        );
                                        return coordinate;
                                    })
                                    .filter(this::inbounds)
                                    .map(c -> lines[c.getY()].charAt(c.getX()))
                                    .collect(StringBuilder::new, StringBuilder::append, (b1, b2) -> b1.append(b2))
                                    .toString();
                        }
                );
    }
}

