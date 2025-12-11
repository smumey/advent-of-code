package me.sol.aoc2025;

import me.sol.Answer;
import me.sol.Utility;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public record D08Playground(List<Coordinate> coordinates, int numConnections) {
    static List<Coordinate> parse(Stream<String> input) {
        return input
                .map(l -> {
                    var scanner = new Scanner(l);
                    scanner.useDelimiter(",");
                    return new Coordinate(scanner.nextLong(), scanner.nextLong(), scanner.nextLong());
                })
                .toList();
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D08Playground(Utility.readInput(D08Playground.class, D08Playground::parse), 1000));
    }

    private static void processPair(HashMap<Coordinate, Set<Coordinate>> map, Pair pair) {
        if (map.containsKey(pair.c1)) {
            var set1 = map.get(pair.c1);
            if (set1.contains(pair.c2)) {
//                    continue;
            } else if (map.containsKey(pair.c2)) {
                var set2 = map.get(pair.c2);
                set1.addAll(set2);
                set2.forEach(c -> map.put(c, set1));
            } else {
                set1.add(pair.c2);
                map.put(pair.c2, set1);
            }
        } else if (map.containsKey(pair.c2)) {
            var set2 = map.get(pair.c2);
            set2.add(pair.c1);
            map.put(pair.c1, set2);
        } else {
            var set = new HashSet<Coordinate>();
            set.add(pair.c1);
            set.add(pair.c2);
            map.put(pair.c1, set);
            map.put(pair.c2, set);
        }
    }

    @Answer
    int p1LargestProduct() {
        var queue = queuePairs();
        var map = new HashMap<Coordinate, Set<Coordinate>>();
        for (var connections = 0; connections < numConnections; ) {
            var pair = queue.poll();
            processPair(map, pair);
            connections++;
        }
        var sets = new HashSet<Set<Coordinate>>(map.values());
        return sets.stream().mapToInt(Set::size).map(s -> -s).sorted().map(s -> -s).limit(3L).reduce((s1, s2) -> s1 * s2).orElseThrow();
    }

    @NotNull
    private PriorityQueue<Pair> queuePairs() {
        var queue = new PriorityQueue<Pair>(coordinates.size() * coordinates.size() / 2 + 1, Comparator.comparing(Pair::squaredDistance));
        for (var i = 0; i < coordinates.size() - 1; i++) {
            for (var j = i + 1; j < coordinates.size(); j++) {
                queue.add(new Pair(coordinates.get(i), coordinates.get(j), coordinates.get(i).squaredDistance(coordinates.get(j))));
            }
        }
        return queue;
    }

    @Answer
    public long p2XCoordProduct() {
        var queue = queuePairs();
        var size = coordinates.size();
        var map = new HashMap<Coordinate, Set<Coordinate>>();
        Pair pair;
        while (true) {
            pair = queue.poll();
            processPair(map, pair);
            if (map.get(pair.c1).size() == size) {
                break;
            }
        }
        return pair.c1.x() * pair.c2.x();
    }

    record Coordinate(long x, long y, long z) {
        long squaredDistance(Coordinate other) {
            var deltaX = other.x - x;
            var deltaY = other.y - y;
            var deltaZ = other.z - z;
            return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
        }
    }

    record Pair(Coordinate c1, Coordinate c2, long squaredDistance) {
    }
}
