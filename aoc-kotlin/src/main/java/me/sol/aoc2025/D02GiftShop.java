package me.sol.aoc2025;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;

public record D02GiftShop(List<Range> ranges) {
    record Range(long low, long high) {
    }

    public static List<Range> parse(Stream<String> lines) {
        return lines.flatMap(l -> Arrays.stream(l.split(","))).map(r -> {
            var bounds = Arrays.stream(r.split("-")).mapToLong(Long::parseLong).toArray();
            return new Range(bounds[0], bounds[1]);
        }).toList();
    }

    private static LongStream checkRangeP1(Range range) {
        return LongStream.rangeClosed(range.low, range.high).filter(id -> {
            var stringId = Long.toString(id);
            return stringId.substring(0, stringId.length() / 2).equals(stringId.substring(stringId.length() / 2));
        });
    }

    private static LongStream checkRangeP2(Range range) {
        var testLengths = new HashMap<Integer, Set<Integer>>();
        return LongStream.rangeClosed(range.low, range.high).filter(id -> {
            var stringId = Long.toString(id);
            return testLengths.computeIfAbsent(
                            stringId.length(), l -> Utility.primeFactors(l)
                                    .stream()
                                    .map(f -> stringId.length() / f)
                                    .collect(toUnmodifiableSet())
                    )
                    .stream()
                    .filter(i -> IntStream.iterate(i, j -> j < stringId.length(), j -> j + i)
                            .allMatch(j -> stringId.substring(j, j + i).equals(stringId.substring(0, i))))
                    .findAny().isPresent();
        });
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D02GiftShop(Utility.readInput(D02GiftShop.class, D02GiftShop::parse)));
    }

    @Answer
    public long sumInvalidP1() {
        return ranges().stream().flatMapToLong(D02GiftShop::checkRangeP1).sum();
    }

    @Answer
    public long sumInvalidP2() {
        return ranges().stream().flatMapToLong(D02GiftShop::checkRangeP2).sum();
    }
}
