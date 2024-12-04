package me.sol.aoc2024;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class D04WordSearch {
    private static final String search = "XMAS";
    private static final String reversedSearch = "SAMX";

    static Puzzle parse(Stream<String> lines) {
        return new Puzzle(lines.toArray(String[]::new));
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

        Stream<String> diagonals(boolean up) {
            var n = size();
            return IntStream.range(0, n)
                    .mapToObj(
                            c -> {
                                var l = n - Math.abs();
                                return IntStream.range(0, l)
                                        .mapToObj(k -> {
                                            var xPos = up ? n - 1 - c - (l / 2) + k : c - (l / 2) + k;
                                            var yPos = up ? c - (l / 2) + k : n - 1 - c - (l / 2) + k;
                                            System.out.printf("up=%b, c=%d, l=%d, xPos=%d, yPos=%d%n", up, c, l, xPos, yPos);
                                            return lines[yPos].charAt(xPos);
                                        })
                                        .collect(StringBuilder::new, StringBuilder::append, (b1, b2) -> b1.append(b2))
                                        .toString();
                            }
                    );
        }
    }


}
