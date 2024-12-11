package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableMap;

public class D11PlutonianPebbles {
    private final Map<Long, Long> pebbles;

    public D11PlutonianPebbles(Map<Long, Long> pebbles) {
        this.pebbles = pebbles;
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D11PlutonianPebbles(Utility.readInput(D11PlutonianPebbles.class, D11PlutonianPebbles::parse)));
    }

    static Map<Long, Long> parse(Stream<String> lines) {
        return Arrays.stream(lines.findFirst().orElseThrow().split(" "))
                .map(Long::parseLong)
                .collect(toUnmodifiableMap(Function.identity(), x -> 1L, (x, y) -> x + y));
    }

    private static Map<Long, Long> blink(Map<Long, Long> map) {
        return map.entrySet().stream()
                .flatMap(e -> {
                    Stream<Long> newStones;
                    if (e.getKey() == 0L) {
                        newStones = Stream.of(1L);
                    } else {
                        var stoneString = e.getKey().toString();
                        int l = stoneString.length();
                        if (l % 2 == 0) {
                            newStones = Stream.of(stoneString.substring(0, l / 2), stoneString.substring(l / 2)).map(Long::parseLong);
                        } else {
                            newStones = Stream.of(e.getKey() * 2024L);
                        }
                    }
                    return newStones.map(s -> Map.entry(s, e.getValue()));
                })
                .collect(toUnmodifiableMap(e -> e.getKey(), e -> e.getValue(), (x, y) -> x + y));
    }

    @Answer
    long p1CountPebbles() {
        return countPebbles(25);
    }

    @Answer
    long p2CountPebbles() {
        return countPebbles(75);
    }

    private long countPebbles(int iterations) {
        return Stream.iterate(pebbles, D11PlutonianPebbles::blink).limit(iterations + 1)
//                .peek(System.out::println)
                .reduce((m1, m2) -> m2)
                .orElseThrow()
                .values()
                .stream()
                .mapToLong(c -> c)
                .sum();
    }
}
