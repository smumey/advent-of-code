package me.sol;

import aoc.Direction;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public final class Grid {
    public static final int OUT = -1;
    private final long[][] rows;

    public Grid(long[][] rows) {
        this.rows = rows;
    }

    public int toCoordinate(int x, int y) {
        return y * rows.length + x;
    }

    public int getX(int coordinate) {
        return coordinate % rows.length;
    }

    public int getY(int coordinate) {
        return coordinate / rows.length;
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
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Grid) obj;
        return Objects.equals(this.rows, that.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows);
    }

    @Override
    public String toString() {
        return "Grid[lines=" + rows + ']';
    }

    public int[] findAll(long value) {
        return IntStream.range(0, height())
                .flatMap(j -> IntStream.range(0, width()).map(i -> toCoordinate(i, j)))
                .filter(c -> getValue(c) == value)
                .toArray();
    }

    IntStream streamCoordinates() {
        return IntStream.range(0, rows.length)
                .flatMap(y -> IntStream.range(0, rows[0].length).map(x -> toCoordinate(x, y)));
    }

    public void printAsChars(Writer writer) throws IOException {
        for (int j = 0; j < rows.length; j++) {
            for (int i = 0; i < rows[0].length; i++) {
                writer.append((char) rows[j][i]);
            }
            writer.write(System.lineSeparator());
        }
    }

}
