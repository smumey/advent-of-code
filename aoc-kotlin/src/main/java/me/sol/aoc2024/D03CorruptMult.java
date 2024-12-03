package me.sol.aoc2024;

import me.sol.Utility;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D03CorruptMult {

    private final String input;

    D03CorruptMult(String input) {
        this.input = input;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new D03CorruptMult(Utility.readInput(D03CorruptMult.class, D03CorruptMult::parse)).sumProducts());
    }

    static String parse(Stream<String> lines) {
        return lines.collect(Collectors.joining(System.lineSeparator()));
    }

    long sumProducts() {
        return ParseState.sumProducts(input);
    }

    record ParseState(boolean active, String remaining, long prodSum) {
        static final String ON = "do()";
        static final String OFF = "don't()";
        static final Pattern PROD_PATTERN = Pattern.compile("^mul\\((\\d{1,3}),(\\d{1,3})\\)");

        static long sumProducts(String s) {
            return Stream.iterate(new ParseState(true, s, 0L), ParseState::hasNext, ParseState::transition)
                    .reduce((s1, s2) -> s2)
                    .map(ParseState::prodSum)
                    .orElse(0L);
        }

        ParseState transition() {
            if (remaining.startsWith(ON)) {
                return new ParseState(true, remaining.substring(ON.length()), prodSum);
            } else if (remaining.startsWith(OFF)) {
                return new ParseState(false, remaining.substring(OFF.length()), prodSum);
            } else {
                var matcher = PROD_PATTERN.matcher(remaining);
                if (matcher.find()) {
                    long product = active ? Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2)) : 0L;
                    return new ParseState(active, remaining.substring(matcher.end()), prodSum + product);
                }
                return new ParseState(active, remaining.substring(1), prodSum);
            }
        }

        boolean hasNext() {
            return !remaining.isEmpty();
        }
    }
}
