package me.sol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public final class Utility {
    private Utility() {
    }

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
}
