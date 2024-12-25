package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntBinaryOperator;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toUnmodifiableMap;

public class D24CrossedWires {
    private final Input input;

    public static void main(String[] args) throws IOException {
        Utility.execute(new D24CrossedWires(Utility.readInput(D24CrossedWires.class, D24CrossedWires::parse)));
    }

    static Input parse(Stream<String> lineStream) {
        var lines = lineStream.toList();
        var assignments = lines.stream()
                .takeWhile(not(String::isEmpty))
                .map(l -> {
                    var tokens = l.split(": ");
                    return Map.entry(tokens[0], Integer.parseInt(tokens[1]));
                })
                .collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
        var gateMap = lines.subList(lines.indexOf("") + 1, lines.size()).stream()
                .map(l -> {
                    var tokens = l.split(" ");
                    return Map.entry(tokens[4], new Gate(Operator.valueOf(tokens[1]), tokens[0], tokens[2]));
                })
                .collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
        return new Input(assignments, gateMap);
    }

    D24CrossedWires(Input input) {
        this.input = input;
    }

    @Answer
    long p1ZedValue() {
        var map = new ConcurrentHashMap<>(input.assignments);
        var zedVars = Stream.concat(map.keySet().stream(), input.gates().keySet().stream())
                .filter(var -> var.charAt(0) == 'z')
                .sorted()
                .toList();
        var values = zedVars.stream().map(v -> evaluate(map, v))
                .toList();
        return LongStream.range(0, values.size())
                .reduce(0L, (total, i) -> total + ((long) values.get((int) i) << i));
    }

    private Integer evaluate(ConcurrentHashMap<String, Integer> map, String v) {
        return map.computeIfAbsent(
                v, u -> {
                    var g = input.gates.get(u);
                    var value = g.operator().applyAsInt(evaluate(map, g.wire1()), evaluate(map, g.wire2()));
                    return value;
                }
        );
    }

    enum Operator implements IntBinaryOperator {
        AND((left, right) -> left & right),
        OR((left, right) -> left | right),
        XOR((left, right) -> left ^ right);

        private final IntBinaryOperator operator;

        Operator(IntBinaryOperator operator) {
            this.operator = operator;
        }

        @Override
        public int applyAsInt(int left, int right) {
            return operator.applyAsInt(left, right);
        }
    }

    record Gate(Operator operator, String wire1, String wire2) {
    }

    record Input(Map<String, Integer> assignments, Map<String, Gate> gates) {
    }
}
