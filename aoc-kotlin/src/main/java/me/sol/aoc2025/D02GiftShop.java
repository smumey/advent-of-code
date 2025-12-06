package me.sol.aoc2025;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;

public record D02GiftShop(List<Range> ranges) {
    public static void main(String[] args) throws IOException {
        Utility.execute(new D02GiftShop(Utility.readInput(D02GiftShop.class, D02GiftShop::parse)));
    }

    public static List<Range> parse(Stream<String> lines) {
        return lines.flatMap(l -> Arrays.stream(l.split(","))).map(r -> {
            var bounds = Arrays.stream(r.split("-")).mapToLong(Long::parseLong).toArray();
            return new Range(bounds[0], bounds[1]);
        }).toList();
    }

    private static long checkRangeP1(Range range) {
        var sum = 0L;
        for (var id = range.low; id <= range.high; id++) {
            var stringId = Long.toString(id);
            if (stringId.length() % 2 == 0 && isRepeatingPattern(stringId, stringId.length() / 2)) {
                sum += id;
            }
        }
        return sum;
    }

    private static long checkRangeP2(Range range) {
        var testLengths = new HashMap<Integer, Set<Integer>>();
        var sum = 0L;
        for (var id = range.low; id <= range.high; id++) {
            var stringId = Long.toString(id);
            var length = stringId.length();
            var patternLengths = testLengths.computeIfAbsent(
                    length,
                    l -> Utility.primeFactors(l)
                            .stream()
                            .map(f -> l / f)
                            .collect(toUnmodifiableSet())
            );
            var hasPattern = false;
            for (int patternLength : patternLengths) {
                if (isRepeatingPattern(stringId, patternLength)) {
                    hasPattern = true;
                    break;
                }
            }
            if (hasPattern) {
                sum += id;
            }
        }
        return sum;
    }

    private static boolean isRepeatingPattern(String str, int patternLen) {
        var len = str.length();
        for (var i = patternLen; i < len; i++) {
            if (str.charAt(i) != str.charAt(i % patternLen)) {
                return false;
            }
        }
        return true;
    }

    @Answer
    public long sumInvalidP1() {
        var sum = 0L;
        for (var range : ranges) {
            sum += checkRangeP1(range);
        }
        return sum;
    }

    @Answer
    public long sumInvalidP2() {
        var sum = 0L;
        for (var range : ranges) {
            sum += checkRangeP2(range);
        }
        return sum;
    }

    record Range(long low, long high) {
    }
}
