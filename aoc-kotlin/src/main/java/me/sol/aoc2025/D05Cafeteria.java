package me.sol.aoc2025;

import aoc.RangeKt;
import kotlin.ranges.LongRange;
import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public record D05Cafeteria(List<LongRange> ranges, List<Long> ingredientIds) {
    static D05Cafeteria parse(Stream<String> input) {
        var list = input.toList();
        return new D05Cafeteria(list.stream()
                .takeWhile(not(String::isBlank))
                .map(l -> {
                    try (var scanner = new Scanner(l)) {
                        scanner.useDelimiter("-");
                        return new LongRange(scanner.nextLong(), scanner.nextLong());
                    }
                }).toList(),
                list.stream()
                        .dropWhile(not(String::isBlank))
                        .skip(1)
                        .map(Long::parseLong).toList()
        );
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(Utility.readInput(D05Cafeteria.class, D05Cafeteria::parse));
    }

    @Answer
    int p1FreshCount() {
        var count = 0;
        for (var id : ingredientIds) {
            for (var range : ranges) {
                if (range.contains(id)) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    @Answer
    long p2AllFreshCount() {
        return RangeKt.distinctLongRanges(ranges).stream().mapToLong(r -> r.getEndInclusive() - r.getStart() + 1L).sum();
    }


    record Range(long low, long high) {
        boolean contains(long ingredientId) {
            return low <= ingredientId && ingredientId <= high;
        }
    }

}
