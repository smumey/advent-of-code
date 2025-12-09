package me.sol.aoc2025;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public record D06TrashCompactor(List<String> lines) {
    static D06TrashCompactor parse(Stream<String> input) {
        return new D06TrashCompactor(input.toList());
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(Utility.readInput(D06TrashCompactor.class, D06TrashCompactor::parse));
    }

    List<Problem> parseP1() {
        var scanner = new Scanner(lines.getLast());
        var operators = new ArrayList<Operator>();
        while (scanner.hasNext()) {
            operators.add(Operator.fromSymbol(scanner.next().charAt(0)));
        }
        var numbersLists = operators.stream().map(o -> new ArrayList<Long>()).toList();
        for (var line : lines.subList(0, lines.size() - 1)) {
            var numScanner = new Scanner(line);
            for (var list : numbersLists) {
                list.add(numScanner.nextLong());
            }
        }
        var problems = new ArrayList<Problem>(operators.size());
        for (var i = 0; i < operators.size(); i++) {
            problems.add(new Problem(operators.get(i), Collections.unmodifiableList(numbersLists.get(i))));
        }
        return Collections.unmodifiableList(problems);
    }

    List<Long> parseNumbers(int left, int right) {
        var numbers = IntStream.range(left, right)
                .mapToLong(c -> LongStream.range(0L, lines.size() - 1)
                        .reduce(0L, (sum, r) -> {
                                    var digit = lines.get((int) r).charAt(c);
                                    var d = digit == ' ' ? 0 : digit - '0';
                                    return digit == ' ' ? sum : 10L * sum + d;
                                }
                        ))
                .boxed()
                .toList();
        return numbers;

    }

    List<Problem> parseP2() {
        var opLine = lines.getLast();
        var problems = new ArrayList<Problem>();
        for (var right = opLine.length(); right > 0; ) {
            for (var left = right - 1; left >= 0; left--) {
                var opChar = opLine.charAt(left);
                if (opChar != ' ') {
                    var numbers = parseNumbers(left, right);
                    problems.add(new Problem(Operator.fromSymbol(opChar), numbers));
                    right = left - 1;
                    break;
                }
            }
        }
        return Collections.unmodifiableList(problems);
    }

    @Answer
    long p1Sum() {
        return parseP1().stream().mapToLong(Problem::evaluate).sum();
    }

    @Answer
    long p2Sum() {
        return parseP2().stream().mapToLong(Problem::evaluate).sum();
    }

    enum Operator implements LongBinaryOperator {
        MULTIPLY('*', 1L),
        ADD('+', 0L);

        final char symbol;
        final long identity;

        Operator(char symbol, long identity) {
            this.symbol = symbol;
            this.identity = identity;
        }

        static Operator fromSymbol(int symbol) {
            return switch (symbol) {
                case '*' -> MULTIPLY;
                case '+' -> ADD;
                default -> throw new IllegalArgumentException();
            };
        }

        @Override
        public long applyAsLong(long left, long right) {
            return switch (this) {
                case MULTIPLY -> left * right;
                case ADD -> left + right;
            };
        }
    }

    record Problem(Operator operator, List<Long> operands) {
        long evaluate() {
            return operands.stream().mapToLong(o -> o).reduce(operator.identity, operator);
        }
    }

}
