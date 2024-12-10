package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Grid;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class D08ResonantCollinearity {
    private static final long[] FREQUENCIES = LongStream.range(0, 127)
            .filter(l -> Character.isDigit((int) l) || Character.isAlphabetic((int) l))
            .toArray();
    private final Grid grid;

    D08ResonantCollinearity(Grid grid) {
        this.grid = grid;
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D08ResonantCollinearity(Utility.readInput(D08ResonantCollinearity.class, Utility::parseCharGrid)));
    }

    @Answer
    long p1CountAntinodes() {
        return countAntinodes(this::type1Antinodes);
    }

    private long countAntinodes(UnaryOperator<int[]> getAntinodes) {
        return Arrays.stream(FREQUENCIES)
                .mapToObj(grid::findAll)
                .map(getAntinodes)
                .flatMapToInt(Arrays::stream)
                .filter(a -> a != Grid.OUT)
                .distinct()
                .count();
    }

    @Answer
    long p2CountAntinodes() {
        return countAntinodes(this::allAntinodes);
    }

    private int[] type1Antinodes(int[] antennas) {
        return IntStream.range(0, antennas.length)
                .mapToObj(
                        i -> IntStream.range(i + 1, antennas.length)
                                .flatMap(j -> Arrays.stream(type1Antinodes(antennas[i], antennas[j])))

                )
                .flatMapToInt(a -> a)
                .toArray();
    }

    private int[] allAntinodes(int[] antennas) {
        return IntStream.range(0, antennas.length)
                .mapToObj(
                        i -> IntStream.range(i + 1, antennas.length)
                                .flatMap(j -> IntStream.concat(
                                                Arrays.stream(type1Antinodes(antennas[i], antennas[j])),
                                                Arrays.stream(type2Antinodes(antennas[i], antennas[j]))
                                        )
                                )

                )
                .flatMapToInt(a -> a)
                .filter(a -> a != Grid.OUT)
                .toArray();
    }

    private int[] type1Antinodes(int antenna1, int antenna2) {
        int deltaX = grid.getX(antenna2) - grid.getX(antenna1);
        int deltaY = grid.getY(antenna2) - grid.getY(antenna1);
        var antinodes = new int[4];
        Arrays.setAll(antinodes, i -> Grid.OUT);
        int i = 0;
        if (deltaX % 3 == 0 && deltaY % 3 == 0) {
            antinodes[i++] = grid.move(antenna1, deltaX / 3, deltaY / 3);
            antinodes[i++] = grid.move(antenna2, -deltaX / 3, -deltaY / 3);
        }
        int n1 = grid.move(antenna1, -deltaX, -deltaY);
        if (n1 != Grid.OUT) {
            antinodes[i++] = n1;
        }
        int n2 = grid.move(antenna2, deltaX, deltaY);
        if (n2 != Grid.OUT) {
            antinodes[i] = n2;
        }
        return antinodes;
    }

    private int[] type2Antinodes(int antenna1, int antenna2) {
        int deltaX = grid.getX(antenna2) - grid.getX(antenna1);
        int deltaY = grid.getY(antenna2) - grid.getY(antenna1);
        int gcd = (int) Utility.greatestCommonDenominator(deltaX, deltaY);
        int dX = deltaX / gcd;
        int dY = deltaY / gcd;
        return IntStream.concat(
                IntStream.iterate(antenna1, n -> n != Grid.OUT, n -> grid.move(n, dX, dY)),
                IntStream.iterate(antenna2, n -> n != Grid.OUT, n -> grid.move(n, -dX, -dY))
        ).toArray();
    }

}
