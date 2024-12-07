package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.LongBinaryOperator;
import java.util.stream.Stream;

public class D07BridgeRepair {
    private final Equation[] equations;

    D07BridgeRepair(Equation[] equations) {
        this.equations = equations;
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D07BridgeRepair(Utility.readInput(D07BridgeRepair.class, D07BridgeRepair::parse)));
    }

    static Equation[] parse(Stream<String> lines) {
        return lines
                .map(l -> {
                    var all = Arrays.stream(l.split(":?\\s+")).mapToLong(Long::parseLong).toArray();
                    var numbers = new long[all.length - 1];
                    System.arraycopy(all, 1, numbers, 0, numbers.length);
                    return new Equation(all[0], numbers);
                }).toArray(Equation[]::new);
    }


    @Answer
    long p1CalibrationSum() {
        return Arrays.stream(equations)
                .mapToLong(
                        eq -> eq.solve(List.of(
                                Long::sum,
                                (x, y) -> x * y
                        ))
                ).sum();
    }

    @Answer
    long p2CalibrationSum() {
        return Arrays.stream(equations)
                .mapToLong(eq -> eq.solve(List.of(
                                Long::sum,
                                (x, y) -> x * y,
                                (x, y) -> Long.parseLong(Long.toString(x) + y)
                        ))
                ).sum();
    }

    record Equation(long answer, long[] numbers) {
        long solve(List<LongBinaryOperator> operators) {
            var nodes = new LinkedList<Node>();
            nodes.add(new Node(numbers[0], 0));
            while (!nodes.isEmpty()) {
                var node = nodes.pop();
                if (node.value() == answer && node.depth() == numbers.length - 1) {
                    return answer;
                }
                nodes.addAll(node.children(this, operators));
            }
            return 0L;
        }

    }

    record Node(long value, int depth) {
        List<Node> children(Equation equation, List<LongBinaryOperator> operators) {
            return depth < equation.numbers.length - 1 ? operators.stream()
                    .map(op -> new Node(op.applyAsLong(value, equation.numbers[depth + 1]), depth + 1))
                    .filter(n -> n.value <= equation.answer)
                    .toList()
                    : List.of();
        }
    }
}
