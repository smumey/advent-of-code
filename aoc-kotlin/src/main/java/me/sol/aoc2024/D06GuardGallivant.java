package me.sol.aoc2024;

import aoc.Coordinate;
import aoc.Direction;
import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Map.of;

public class D06GuardGallivant {
    static final Map<Integer, Direction> directionMap = of(
            (int) '^', Direction.UP,
            (int) '<', Direction.LEFT,
            (int) '>', Direction.RIGHT,
            (int) 'v', Direction.DOWN
    );

    private final int[][] labMap;

    D06GuardGallivant(int[][] labMap) {
        this.labMap = labMap;
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D06GuardGallivant(Utility.readInput(D06GuardGallivant.class, D06GuardGallivant::parse)));
    }

    private static int getCell(int[][] map, Coordinate c) {
        return map[c.getY()][c.getX()];
    }

    private static void setCell(int[][] map, Coordinate c, int chr) {
        map[c.getY()][c.getX()] = chr;
    }

    static int[][] parse(Stream<String> lines) {
        return lines
                .map(String::chars)
                .map(IntStream::toArray)
                .toArray(int[][]::new);
    }

    private static int[][] copy(int[][] map) {
        var c = new int[map.length][];
        for (int i = 0; i < c.length; i++) {
            c[i] = map[i].clone();
        }
        return c;
    }

    private Coordinate getInitialGuardPos() {
        return IntStream.range(0, labMap.length)
                .boxed()
                .flatMap(y -> IntStream.range(0, labMap[0].length).mapToObj(x -> new Coordinate(x, y)))
                .filter(c -> directionMap.containsKey(getCell(labMap, c)))
                .findFirst()
                .orElseThrow();
    }

    @Answer
    int distinctGuardPositionCount() {
        var guardPos = getInitialGuardPos();
        var guardDir = directionMap.get(getCell(labMap, guardPos));
        var map = copy(labMap);
        var posCount = 1;
        var inbounds = (Predicate<Coordinate>) c -> 0 <= c.getY() && c.getY() < map.length && 0 <= c.getX() && c.getX() < map[0].length;
        setCell(map, guardPos, '@');
        while (inbounds.test(guardPos)) {
            var newPos = guardPos.move(guardDir);
            if (inbounds.test(newPos)) {
                switch (getCell(map, newPos)) {
                    case '#':
                        guardDir = Direction.values()[(guardDir.ordinal() + 1) % Direction.values().length];
                        break;
                    case '@':
                        guardPos = newPos;
                        break;
                    default:
                        guardPos = newPos;
                        posCount += 1;
                        setCell(map, guardPos, '@');
                        break;
                }
            } else {
                guardPos = newPos;
            }
        }
        return posCount;
    }
}
