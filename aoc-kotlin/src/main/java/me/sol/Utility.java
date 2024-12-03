package me.sol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;
import java.util.stream.Stream;

public final class Utility {
    public static <T> T parseInput(Class<?> clazz, Function<Stream<String>, T> transform) throws IOException {
        return parse(clazz, transform, "input");
    }

    public static <T> T parseSample(Class<?> clazz, Function<Stream<String>, T> transform) throws IOException {
        return parse(clazz, transform, "sample");
    }

    private static <T> T parse(Class<?> clazz, Function<Stream<String>, T> transform, String fileSuffix) throws IOException {
        var dirName = clazz.getPackageName().replaceFirst(".*\\.", "");
        var inputName = "/input/%s/%s-%s".formatted(dirName, clazz.getSimpleName().substring(0, 3).toLowerCase(), fileSuffix);
        try (var reader = new BufferedReader(new InputStreamReader(clazz.getResourceAsStream(inputName)))) {
            return transform.apply(reader.lines());
        }
    }

    private Utility() {
    }
}
