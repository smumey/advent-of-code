package me.sol.aoc2024;

import aoc.Coordinate;
import aoc.Direction;
import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class D06GuardGallivant {
    static final Map<Integer, Direction> directionMap = Map.of(
            (int) '^', Direction.UP,
            (int) '<', Direction.LEFT,
            (int) '>', Direction.RIGHT,
            (int) 'v', Direction.DOWN
    );

    static final Map<Direction, Integer> dirSymbolMap = directionMap.entrySet().stream()
            .collect(toMap(Map.Entry::getValue, Map.Entry::getKey));

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

    private static int[][] makeBlankMap(int[][] map) {
        var c = new int[map.length][];
        for (int i = 0; i < c.length; i++) {
            c[i] = new int[map[i].length];
        }
        return c;
    }

    private static Direction turnRight(Direction d) {
        return Direction.values()[(d.ordinal() + 1) % Direction.values().length];
    }

    private static void copyTo(int[][] source, int[][] target) {
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, target[i], 0, source.length);
        }
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
        var map = makeBlankMap(labMap);
        copyTo(labMap, map);
        var posCount = 1;
        setCell(map, guardPos, '@');
        while (inbounds(map, guardPos)) {
            var newPos = guardPos.move(guardDir);
            if (inbounds(map, newPos)) {
                switch (getCell(map, newPos)) {
                    case '#':
                        guardDir = turnRight(guardDir);
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

    boolean inbounds(int[][] map, Coordinate c) {
        return 0 <= c.getY() && c.getY() < map.length && 0 <= c.getX() && c.getX() < map[0].length;
    }

    boolean isLoop(int[][] map, GuardState guardState) {
        var previous = new HashSet<GuardState>();
        while (!previous.contains(guardState)) {
            previous.add(guardState);
            var nextPos = guardState.pos().move(guardState.dir());
            if (!inbounds(map, nextPos)) {
                return false;
            }
            if (getCell(map, nextPos) == '#') {
                guardState = new GuardState(guardState.pos(), turnRight(guardState.dir()));
            } else {
                guardState = new GuardState(nextPos, guardState.dir());
            }
        }
        return true;
    }

    @Answer
    int guardLoopBlockCount() {
        var guardPos = getInitialGuardPos();
        var guardDir = directionMap.get(getCell(labMap, guardPos));
        var blocks = new HashSet<Coordinate>();
        var notBlocks = new HashSet<Coordinate>();
        while (inbounds(labMap, guardPos)) {
            var nextPos = guardPos.move(guardDir);
            if (inbounds(labMap, nextPos)) {
                int nextCell = getCell(labMap, nextPos);
                if (nextCell == '#') {
                    guardDir = turnRight(guardDir);
                    continue;
                } else if (!blocks.contains(nextPos) && !notBlocks.contains(nextPos)) {
                    setCell(labMap, nextPos, '#');
                    if (isLoop(labMap, new GuardState(guardPos, guardDir))) {
                        blocks.add(nextPos);
                    } else {
                        notBlocks.add(nextPos);
                    }
                    setCell(labMap, nextPos, nextCell);
                }
            }
            guardPos = nextPos;
        }
        return blocks.size();
    }

    record GuardState(Coordinate pos, Direction dir) {
    }

}
