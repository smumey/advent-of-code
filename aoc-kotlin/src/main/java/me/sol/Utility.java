package me.sol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;
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
}
