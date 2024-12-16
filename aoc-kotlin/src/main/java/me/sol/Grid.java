package me.sol;

import aoc.Direction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.stream.IntStream;

public final class Grid {
    public static final int OUT = -1;
    private final long[][] rows;

    public Grid(long[][] rows) {
        this.rows = rows;
    }

    public int toCoordinate(int x, int y) {
        return y * width() + x;
    }

    public int getX(int coordinate) {
        return coordinate % width();
    }

    public int getY(int coordinate) {
        return coordinate / width();
    }

    public boolean inbounds(int x, int y) {
        return 0 <= x && x < rows[0].length && 0 <= y && y < rows.length;
    }

    public int move(int coordinate, Direction direction) {
        int x = getX(coordinate);
        int y = getY(coordinate);
        int newX = switch (direction) {
            case UP, DOWN -> x;
            case RIGHT -> x + 1;
            case LEFT -> x - 1;
        };
        int newY = switch (direction) {
            case UP -> y - 1;
            case RIGHT, LEFT -> y;
            case DOWN -> y + 1;
        };
        return inbounds(newX, newY) ? toCoordinate(newX, newY) : OUT;
    }

    public long getValue(int coordinate) {
        return rows[getY(coordinate)][getX(coordinate)];
    }

    public void setValue(int coordinate, long value) {
        rows[getY(coordinate)][getX(coordinate)] = value;
    }

    public int move(int origin, int deltaX, int deltaY) {
        int newX = getX(origin) + deltaX;
        int newY = getY(origin) + deltaY;
        return inbounds(newX, newY) ? toCoordinate(newX, newY) : OUT;
    }

    public IntStream neighbours(int origin) {
        return Arrays.stream(Direction.values())
                .mapToInt(d -> move(origin, d))
                .filter(n -> n != OUT);
    }

    public String coordToString(int coordinate) {
        return "(%d,%d)".formatted(getX(coordinate), getY(coordinate));
    }

    public int width() {
        return rows[0].length;
    }

    public int height() {
        return rows.length;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        var that = (Grid) obj;
        return Arrays.deepEquals(rows, that.rows);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(rows);
    }

    @Override
    public String toString() {
        return "Grid[lines=" + rows + ']';
    }

    public int[] findAll(long value) {
        return IntStream.range(0, height())
                .flatMap(y -> IntStream.range(0, width()).map(x -> toCoordinate(x, y)))
                .filter(c -> getValue(c) == value)
                .toArray();
    }

    IntStream streamCoordinates() {
        return IntStream.range(0, rows.length)
                .flatMap(y -> IntStream.range(0, rows[0].length).map(x -> toCoordinate(x, y)));
    }

    public void printAsChars(BufferedWriter writer) {
        try {
            for (int y = 0; y < rows.length; y++) {
                for (int x = 0; x < rows[0].length; x++) {
                    writer.write((int) rows[y][x]);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void printAsChars(OutputStream outputStream) {
        try (var writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            printAsChars(writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Grid copy() {
        return new Grid(Utility.copy(rows));
    }

    public long[][] getRows() {
        return Utility.copy(rows);
    }
}
