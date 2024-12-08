package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
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
        return Arrays.stream(FREQUENCIES)
                .mapToObj(grid::findAll)
                .map(this::antinodes)
                .flatMapToInt(Arrays::stream)
                .filter(a -> a != Grid.OUT)
                .distinct()
                .count();
    }

    private int[] antinodes(int[] antennas) {
        return IntStream.range(0, antennas.length)
                .mapToObj(
                        i -> IntStream.range(i + 1, antennas.length)
                                .flatMap(j -> Arrays.stream(antinodes(antennas[i], antennas[j])))

                )
                .flatMapToInt(a -> a)
                .toArray();
    }

    private int[] antinodes(int antenna1, int antenna2) {
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
            antinodes[i++] = n2;
        }
        return antinodes;
    }
}
