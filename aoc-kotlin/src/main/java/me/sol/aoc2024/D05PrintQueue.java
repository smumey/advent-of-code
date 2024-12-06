package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.mapping;

public class D05PrintQueue {
    private final PageEdit pageEdit;

    D05PrintQueue(PageEdit pageEdit) {
        this.pageEdit = pageEdit;
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D05PrintQueue(Utility.readInput(D05PrintQueue.class, D05PrintQueue::parse)));
    }

    static PageEdit parse(Stream<String> lines) {
        var list = lines.toList();
        return new PageEdit(
                list.stream()
                        .takeWhile(not(String::isBlank))
                        .map(l -> {
                            try (var scanner = new Scanner(l)) {
                                scanner.useDelimiter("\\|");
                                return new PrintDependency(scanner.nextInt(), scanner.nextInt());
                            }
                        })
                        .toArray(PrintDependency[]::new),
                list.stream()
                        .dropWhile(not(String::isBlank))
                        .skip(1)
                        .map(l -> Arrays.stream(l.split(",")).mapToInt(Integer::parseInt).toArray())
                        .toArray(int[][]::new)
        );
    }

    private Map<Integer, Set<Integer>> disallowed() {
        return Arrays.stream(pageEdit.dependencies)
                .collect(Collectors.groupingBy(
                        PrintDependency::after,
                        mapping(PrintDependency::before, Collectors.toSet())
                ));
    }

    private boolean isCorrectlyOrdered(int[] update, Map<Integer, Set<Integer>> disallowed) {
        for (int i = 0; i < update.length; i++) {
            var exclude = disallowed.computeIfAbsent(update[i], x -> Set.of());
            for (int j = i + 1; j < update.length; j++) {
                if (exclude.contains(update[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Answer
    int middlePageSum() {
        var disallowed = disallowed();
        return Arrays.stream(pageEdit.updates())
                .filter(update -> isCorrectlyOrdered(update, disallowed))
                .mapToInt(update -> update[update.length / 2])
                .sum();
    }

    record PrintDependency(int before, int after) {
    }

    record PageEdit(PrintDependency[] dependencies, int[][] updates) {
    }
}
