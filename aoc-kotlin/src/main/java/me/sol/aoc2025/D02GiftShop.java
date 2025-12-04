package me.sol.aoc2025;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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
        return LongStream.rangeClosed(range.low, range.high).filter(id -> {
            var stringId = Long.toString(id);
            return IntStream.rangeClosed(1, stringId.length() / 2).filter(i -> stringId.length() % i == 0)
                    .filter(i -> IntStream.iterate(0, j -> j < stringId.length(), j -> j + i)
                            .allMatch(j -> stringId.substring(j, j + i).equals(stringId.substring(0, i)))).count() > 0;
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
