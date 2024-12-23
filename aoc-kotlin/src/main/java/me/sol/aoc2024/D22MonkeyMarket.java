package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class D22MonkeyMarket {
    private final List<Long> initialNumbers;

    public static final void main(String[] args) throws IOException {
        Utility.execute(new D22MonkeyMarket(Utility.readInput(D22MonkeyMarket.class, D22MonkeyMarket::parse)));
    }

    static List<Long> parse(Stream<String> lines) {
        return lines.map(Long::parseLong).toList();
    }

    private static final long mix(long n1, long n2) {
        return n1 ^ n2;
    }

    private static final long next(long n) {
        var n1 = prune(mix(n, n * 64L));
        var n2 = prune(mix(n1, n1 / 32L));
        return prune(mix(n2, n2 * 2048L));
    }

    private static final long prune(long n) {
        return n % 16777216;
    }

    private static LongStream secretSequence(long initial) {
        return LongStream.iterate(initial, D22MonkeyMarket::next);
    }

    D22MonkeyMarket(List<Long> initialNumbers) {
        this.initialNumbers = initialNumbers;
    }

    @Answer
    long p1SumSecretNumbers() {
        return initialNumbers.stream()
                .map(D22MonkeyMarket::secretSequence)
                .map(s -> s.limit(2001))
                .mapToLong(s -> s.reduce((n1, n2) -> n2).orElseThrow())
                .sum();

    }

    @Answer
    long p2MaximumBananas() {
        var numbers = initialNumbers.stream()
                .map(D22MonkeyMarket::secretSequence)
                .map(s -> s.map(n -> n % 10L))
                .map(s -> s.limit(2001))
                .map(s -> s.boxed().toList())
                .toList();
        var diffs = numbers.stream()
                .map(s -> Utility.sliding(s, 2))
                .map(pairs -> pairs.map(pair -> pair.get(1) - pair.get(0)).toList())
                .toList();
        var changes = diffs.stream()
                .map(s -> Utility.sliding(s, 4).toList())
                .toList();
        var priceMap = IntStream.range(0, numbers.size()).mapToObj(i -> IntStream.range(0, changes.get(i).size())
                        .boxed()
                        .collect(toMap(j -> changes.get(i).get(j), j -> numbers.get(i).get(j + 4), (p1, p2) -> p1))
                ).flatMap(m -> m.entrySet().stream())
                .collect(toMap(e -> e.getKey(), e -> e.getValue(), (p1, p2) -> p1 + p2));
        return priceMap.values().stream().mapToLong(p -> p).max().orElseThrow();
    }

}
