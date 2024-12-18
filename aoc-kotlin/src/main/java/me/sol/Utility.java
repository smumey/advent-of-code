package me.sol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Utility {
    public static <T> T readInput(Class<?> clazz, Function<Stream<String>, T> parser) throws IOException {
        return read(clazz, parser, "input");
    }

    public static <T> T readSample(Class<?> clazz, Function<Stream<String>, T> parser) throws IOException {
        return read(clazz, parser, "sample");
    }

    private static <T> T read(Class<?> clazz, Function<Stream<String>, T> parser, String fileSuffix) throws IOException {
        var dirName = clazz.getPackageName().replaceFirst(".*\\.", "");
        var inputName = "/input/%s/%s-%s".formatted(dirName, clazz.getSimpleName().substring(0, 3).toLowerCase(), fileSuffix);
        try (var reader = new BufferedReader(new InputStreamReader(clazz.getResourceAsStream(inputName)))) {
            return parser.apply(reader.lines());
        }
    }

    public static void execute(Object o) {
        System.out.printf("Executing %s%n", o.getClass().getSimpleName());
        Arrays.stream(o.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Answer.class))
                .forEach(method -> {
                    try {
                        method.setAccessible(true);
                        var start = Instant.now();
                        var result = method.invoke(o);
                        System.out.printf("  %s: %s (%s)%n", method.getName(), result, Duration.between(start, Instant.now()));
                    } catch (Exception e) {
                        Logger.getLogger(o.getClass().getSimpleName()).log(Level.SEVERE, "Exception executing %s", e);
                        throw new RuntimeException(e);
                    }
                });
    }

    public static <T> Grid parseCharGrid(Stream<String> lines) {
        return new Grid(
                lines.map(l -> l.chars().mapToLong(c -> c).toArray()).toArray(long[][]::new)
        );
    }

    public static long greatestCommonDenominator(long a, long b) {
        if (b == 0) {
            return a;
        }
        return greatestCommonDenominator(b, a % b);
    }

    public static <T> Stream<List<T>> sliding(List<T> list, int size) {
        if (size > list.size()) {
            return Stream.empty();
        }
        return IntStream.range(0, list.size() - size + 1)
                .mapToObj(start -> list.subList(start, start + size));
    }

    public static long[][] copy(long[][] map) {
        return Arrays.stream(map)
                .map(r -> r.clone())
                .toArray(long[][]::new);
    }

    public static <S> PathResponse<S> findShortestPath(List<? extends S> nodes, Function<S, Map<S, Long>> edgeGenerator, S source) {
        var start = System.currentTimeMillis();
        var distances = new HashMap<S, Long>();
        var dist = (ToLongFunction<S>) node -> distances.computeIfAbsent(node, n -> Long.MAX_VALUE);
        distances.put(source, 0L);
        var queue = new PriorityQueue<S>(nodes.size(), Comparator.comparing(node -> dist.applyAsLong(node)));
        var active = new HashSet<S>(nodes);
        queue.addAll(nodes);
        var previous = new HashMap<S, S>();
        while (!queue.isEmpty()) {
            var u = queue.poll();
            active.remove(u);
            long distU = dist.applyAsLong(u);
            var edges = edgeGenerator.apply(u);
            edges.forEach((v, edgeDist) -> {
                if (active.contains(v)) {
                    var distV = dist.applyAsLong(v);
                    var altDist = distU + edgeDist;
                    if (altDist < distV) {
                        queue.remove(v);
                        distances.put(v, altDist);
                        queue.offer(v);
                        previous.put(v, u);
                    }
                }
            });
        }
        Logger.getLogger(Utility.class.getName()).log(Level.INFO, "shortest path took %d ms".formatted(System.currentTimeMillis() - start));
        return new PathResponse<>(dist, n -> Optional.ofNullable(previous.get(n)));
    }

    public static int[] parseInts(String line) {
        return Arrays.stream(line.split("[\\s,]+"))
                .filter(s -> s.matches("^-?\\d+$"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private Utility() {
    }

    public record PathResponse<S>(ToLongFunction<S> distances, Function<S, Optional<S>> previous) {
    }
}
