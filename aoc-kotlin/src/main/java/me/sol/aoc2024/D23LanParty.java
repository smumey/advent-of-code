package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
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

//    algorithm BronKerbosch1(R, P, X) is
//    if P and X are both empty then
//    report R as a maximal clique
//    for each vertex v in P do
//    BronKerbosch1(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
//    P := P \ {v}
//    X := X ⋃ {v}

    @Answer
    long p1TripletsCount() {
        var sorted = pairs.stream()
                .sorted(Comparator.<SortedSet<String>, String>comparing(SortedSet::first))
                .toList();
        return IntStream.range(0, sorted.size())
                .boxed()
                .flatMapToInt(i -> IntStream.range(i + 1, sorted.size())
                        .takeWhile(j -> sorted.get(i).first().equals(sorted.get(j).first()))
                        .filter(j ->
                                (
                                        sorted.get(i).first().charAt(0) == 't' ||
                                                sorted.get(i).last().charAt(0) == 't' ||
                                                sorted.get(j).last().charAt(0) == 't'
                                ) &&
                                        pairs.contains(new TreeSet<>(Set.of(sorted.get(i).last(), sorted.get(j).last()))))
                )
                .count();
    }

    @Answer
    String p2LargestClique() {
        var vertices = pairs.stream()
                .flatMap(p -> p.stream())
                .distinct()
                .collect(Collectors.toUnmodifiableSet());
        var edgeMap = pairs.stream()
                .flatMap(p -> Stream.of(Map.entry(p.first(), p.last()), Map.entry(p.last(), p.first())))
                .collect(Collectors.toUnmodifiableMap(
                        e -> e.getKey(),
                        e -> new HashSet<>(Set.of(e.getValue())),
                        (s1, s2) -> {
                            s1.addAll(s2);
                            return s1;
                        }
                ));
        var maximalSets = bronKerbosch(vertices, edgeMap);
        return maximalSets.stream().sorted(Comparator.<Set<String>, Integer>comparing(Set::size).reversed())
                .findFirst().stream()
                .flatMap(Set::stream)
                .sorted()
                .collect(Collectors.joining(","));
    }

    private void bronKerbosch(
            Set<String> r,
            Set<String> p,
            Set<String> x,
            Map<String, ? extends Set<String>> edges,
            Set<Set<String>> maximalCliques
    ) {
        if (p.isEmpty() && x.isEmpty()) {
            maximalCliques.add(r);
        }
        var activeP = new HashSet<>(p);
        for (var v : p) {
            var newR = new HashSet<>(r);
            newR.add(v);
            var newP = new HashSet<>(activeP);
            newP.retainAll(edges.get(v));
            var newX = new HashSet<>(x);
            newX.retainAll(edges.get(v));
            bronKerbosch(newR, newP, newX, edges, maximalCliques);
            activeP.remove(v);
            x.add(v);
        }
    }

    private Set<Set<String>> bronKerbosch(Set<String> vertices, Map<String, ? extends Set<String>> edges) {
        var maximalCliques = new HashSet<Set<String>>();
        bronKerbosch(new HashSet<>(), new HashSet<>(vertices), new HashSet<>(), edges, maximalCliques);
        return maximalCliques;
    }

}
