package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;

public class D23LanParty {
    private final Set<? extends SortedSet<String>> pairs;

    public static void main(String[] args) throws IOException {
        Utility.execute(new D23LanParty(Utility.readInput(D23LanParty.class, D23LanParty::parse)));
    }

    static Set<? extends SortedSet<String>> parse(Stream<String> lines) {
        return lines
                .map(l -> new TreeSet<>(Arrays.asList(l.split("-"))))
                .collect(toUnmodifiableSet());
    }

    D23LanParty(Set<? extends SortedSet<String>> pairs) {
        this.pairs = pairs;
    }

    @Answer
    long p1TripletsCount() {
        var sorted = pairs.stream()
                .sorted(Comparator.<SortedSet<String>, String>comparing(SortedSet::first))
                .toList();
        return IntStream.range(0, sorted.size())
                .boxed()
                .flatMapToInt(i -> IntStream.range(i + 1, sorted.size())
                        .takeWhile(j -> sorted.get(i).first().equals(sorted.get(j).first()))
                        .filter(j -> (
                                sorted.get(i).first().charAt(0) == 't' ||
                                        sorted.get(i).last().charAt(0) == 't' ||
                                        sorted.get(j).last().charAt(0) == 't'
                        ) &&
                                pairs.contains(Set.of(sorted.get(i).last(), sorted.get(j).last())))
                )
                .count();
    }

}
