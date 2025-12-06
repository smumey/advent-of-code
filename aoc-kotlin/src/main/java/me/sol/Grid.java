package me.sol;

import aoc.Direction;

import java.io.*;
import java.util.Arrays;
import java.util.stream.IntStream;

public record Grid(long[][] rows) {
    public static final int OUT = -1;

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
        return move(coordinate, direction, 1);
    }

    public int move(int coordinate, Direction direction, int delta) {
        if (coordinate == OUT) {
            return OUT;
        }
        int x = getX(coordinate);
        int y = getY(coordinate);
        int newX = switch (direction) {
            case UP, DOWN -> x;
            case RIGHT -> x + delta;
            case LEFT -> x - delta;
        };
        int newY = switch (direction) {
            case UP -> y - delta;
            case RIGHT, LEFT -> y;
            case DOWN -> y + delta;
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

    public int getSurroundingCount(int origin, long value) {
        var count = 0;
        for (var deltaY = -1; deltaY <= 1; deltaY++) {
            for (var deltaX = -1; deltaX <= 1; deltaX++) {
                if (!(deltaX == 0 && deltaY == 0)) {
                    var coord = move(origin, deltaX, deltaY);
                    if (coord != OUT && getValue(coord) == value) {
                        count++;
                    }
                }
            }
        }
        return count;
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

    public int distance(int pos1, int pos2) {
        return Math.abs(getX(pos2) - getX(pos1)) + Math.abs(getY(pos2) - getY(pos1));
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

    @Override
    public long[][] rows() {
        return Utility.copy(rows);
    }
}
