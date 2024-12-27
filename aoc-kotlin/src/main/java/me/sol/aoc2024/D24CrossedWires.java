package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntBinaryOperator;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableMap;
import static me.sol.aoc2024.D24CrossedWires.Operator.AND;
import static me.sol.aoc2024.D24CrossedWires.Operator.XOR;

public class D24CrossedWires {
    private final Input input;
    private final int swapsNeeded;

    public static void main(String[] args) throws IOException {
        Utility.execute(new D24CrossedWires(Utility.readInput(D24CrossedWires.class, D24CrossedWires::parse), 4));
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

    D24CrossedWires(Input input, int swapsNeeded) {
        this.input = input;
        this.swapsNeeded = swapsNeeded;
    }

    @Answer
    long p1ZedValue() {
        var map = new ConcurrentHashMap<>(input.assignments);
        var zedVars = getVars('z');
        return getValue(zedVars, map);
    }

    @Answer
    String p2SwappedOutputWires() {
        var map = new ConcurrentHashMap<>(input.assignments);
        var xVars = getVars('x');
        var yVars = getVars('y');
        var zVars = getVars('z');
        var gates = input.gates();
        var carry = 0;
        var swaps = new HashSet<Swap>();
        String andWire;
        String xorWire;
        String carryWire = null;
        String zWire;
        var outputMap = reverse();

        for (int i = 0; i< zVars.size(); i++) {
            andWire = findOutputWire(outputMap, AND, makeWire('x', i), makeWire('y', i));
            xorWire = findOutputWire(outputMap, XOR, makeWire('x', i), makeWire('y', i));
            zWire = carryWire == null ? xorWire : fin

        }
    }

    String makeWire(char prefix, int index) {
        return "%c%00d".formatted(prefix, index);
    }

    String findOutputWire(Map<Gate, String> outputMap, Operator operator, String wire1, String wire2) {
        var wire = outputMap.get(new Gate(operator, wire1, wire2));
        return wire == null ? outputMap.get(new Gate(operator, wire2, wire1)) : wire;
    }


    Map<Gate, String> reverse() {
        return input.gates.entrySet().stream()
                .collect(toMap(e -> e.getValue(), e -> e.getKey()));
    }

    record Swap(String wire1, String wire2) {

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

    private long getValue(List<String> vars, ConcurrentHashMap<String, Integer> map) {
        var values = vars.stream().map(v -> evaluate(map, v))
                .toList();
        return LongStream.range(0, values.size())
                .reduce(0L, (total, i) -> total + ((long) values.get((int) i) << i));
    }

    @NotNull
    private List<String> getVars(int prefix) {
        return Stream.concat(input.assignments.keySet().stream(), input.gates().keySet().stream())
                .filter(var -> var.charAt(0) == prefix)
                .sorted()
                .toList();
    }

    private Set<String> inputWires(String wire) {
        return input.gates().compute(
                wire,
                (var, gate) ->
                )
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
