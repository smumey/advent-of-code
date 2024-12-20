package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class D19LinenLayout {
    private final DesignPatterns designPatterns;
    private final Map<String, Long> combinations = new HashMap<>();
    private final Set<String> matched = new HashSet<>();
    private final Set<String> unmatched = new HashSet<>();

    public static void main(String[] args) throws IOException {
        Utility.execute(new D19LinenLayout(Utility.readInput(D19LinenLayout.class, D19LinenLayout::parse)));
    }

    static DesignPatterns parse(Stream<String> lineStream) {
        var lines = lineStream.toList();
        return new DesignPatterns(
                Arrays.asList(lines.getFirst().split(" *, *")),
                lines.subList(2, lines.size())
        );
    }

    D19LinenLayout(DesignPatterns designPatterns) {
        this.designPatterns = designPatterns;
    }

    @Answer
    long p1CountMatched() {
        return designPatterns.designs().stream().peek(System.out::println).filter(this::isMatched).count();
    }

    @Answer
    long p2CountUnique() {
        return designPatterns.designs().stream()
                .peek(System.out::println)
                .mapToLong(this::findCombinations)
                .sum();
    }

    boolean isMatched(String design) {
        if (matched.contains(design)) {
            return true;
        }
        if (unmatched.contains(design)) {
            return false;
        }
        var found = design.isEmpty() || designPatterns.patterns().stream()
                .anyMatch(p -> design.startsWith(p) && isMatched(design.substring(p.length())));
        if (found) {
            matched.add(design);
        } else {
            unmatched.add(design);
        }
        return found;
    }

    long findCombinations(String design) {
        if (combinations.containsKey(design)) {
            return combinations.get(design);
        }
        long count;
        if (design.isEmpty()) {
            count = 1;
        } else {
            count = designPatterns.patterns.stream()
                    .mapToLong(p -> design.startsWith(p) ? findCombinations(design.substring(p.length())) : 0L)
                    .sum();
        }
        combinations.put(design, count);
        return count;
    }

    record DesignPatterns(List<String> patterns, List<String> designs) {
        DesignPatterns {
            patterns = List.copyOf(patterns);
            designs = List.copyOf(designs);
        }
    }
}
