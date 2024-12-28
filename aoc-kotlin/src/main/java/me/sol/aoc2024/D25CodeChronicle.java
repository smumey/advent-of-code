package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class D25CodeChronicle {
    private final Input input;

    public static void main(String[] args) throws IOException {
        Utility.execute(new D25CodeChronicle(Utility.readInput(D25CodeChronicle.class, D25CodeChronicle::parse)));
    }

    static Input parse(Stream<String> lineStream) {
        var locks = new ArrayList<Mechanism>();
        var keys = new ArrayList<Mechanism>();
        var iterator = lineStream.iterator();
        while (iterator.hasNext()) {
            var line = iterator.next();
            if (line.chars().allMatch(c -> c == '#')) {
                locks.add(parseMechansism(iterator, line.length(), 0));
            } else {
                keys.add(parseMechansism(iterator, line.length(), -1));
            }
        }
        return new Input(locks, keys);
    }

    static Mechanism parseMechansism(Iterator<String> iterator, int numberOfLocks, int start) {
        var values = new int[numberOfLocks];
        int height = 0;
        while (iterator.hasNext()) {
            var line = iterator.next();
            if (line.isEmpty()) {
                break;
            }
            height += 1;
            IntStream.range(0, line.length()).forEach(i -> {
                if (line.charAt(i) == '#') {
                    values[i] += 1;
                }
            });
        }
        return new Mechanism(height, values);
    }

    D25CodeChronicle(Input input) {
        this.input = input;
    }

    @Answer
    long p1NumMatchingPairs() {
        return input.locks().stream().flatMap(lock -> input.keys().stream().filter(k -> lock.canMatch(k))).count();
    }

    record Input(List<Mechanism> locks, List<Mechanism> keys) {
    }

    record Mechanism(int height, int[] values) {
        boolean canMatch(Mechanism other) {
            return IntStream.range(0, values.length).allMatch(i -> values[i] + other.values[i] <= height);
        }
    }
}
