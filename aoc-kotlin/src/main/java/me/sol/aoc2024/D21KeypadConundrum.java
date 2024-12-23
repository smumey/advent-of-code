package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Grid;
import me.sol.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class D21KeypadConundrum {
    private static final Grid DIRECTIONAL_GRID = new Grid(
            """
                     ^A
                    <v>
                    """.lines()
                    .map(l -> l.chars().asLongStream().toArray())
                    .toArray(long[][]::new)
    );
    private static final Grid NUMERIC_GRID = new Grid(
            """
                    789
                    456
                    123
                     0A
                    """.lines()
                    .map(l -> l.chars().asLongStream().toArray())
                    .toArray(long[][]::new)
    );
    private final Map<CodeDepth, Long> calculatedCounts = new HashMap<>();
    private final List<String> codes;
    private final Map<Move, Set<String>> computedMoves = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Utility.execute(new D21KeypadConundrum(Utility.readInput(D21KeypadConundrum.class, D21KeypadConundrum::parse)));
    }

    static List<String> parse(Stream<String> lineStream) {
        return lineStream.toList();
    }

    D21KeypadConundrum(List<String> codes) {
        this.codes = codes;
    }

    @Answer
    long p1ComplexitySum() {
        var commandCounts = codes.stream()
                .map(code -> count(code, 3))
                .toList();
        System.out.printf("commandCounts=%s%n", commandCounts);
        return IntStream.range(0, commandCounts.size())
                .mapToLong(i -> commandCounts.get(i) * Integer.parseInt(codes.get(i)
                        .substring(0, codes.get(i).length() - 1)))
                .sum();
    }

    @Answer
    long p2ComplexitySum() {
        var commandCounts = codes.stream()
                .map(code -> count(code, 26))
                .toList();
        System.out.printf("commandCounts=%s%n", commandCounts);
        return IntStream.range(0, commandCounts.size())
                .mapToLong(i -> commandCounts.get(i) * Integer.parseInt(codes.get(i)
                        .substring(0, codes.get(i).length() - 1)))
                .sum();
    }

    @Answer
    long test() {
        var commands = reverse(DIRECTIONAL_GRID, reverse(DIRECTIONAL_GRID, reverse(NUMERIC_GRID, "5")));
        System.out.println(commands);
        return 0;
    }

    private long count(String code, int depth) {
        var codeDepth = new CodeDepth(code, depth);
        if (calculatedCounts.containsKey(codeDepth)) {
            return calculatedCounts.get(codeDepth);
        }
        System.out.printf("calculating case for code %s depth %d%n", code, depth);
        long count;
        if (depth == 0) {
            count = code.length();
        } else {
            long total = 0;
            int prev = 'A';
            for (int c : code.toCharArray()) {
                long min = Long.MAX_VALUE;
                for (var move : moves(prev, c)) {
                    min = Math.min(min, count(move, depth - 1));
                }
                total += min;
                prev = c;
            }
            count = total;
        }
        calculatedCounts.put(codeDepth, count);
        return count;
    }

    private int distance(String commands) {
        var dist = 0;
        var grid = DIRECTIONAL_GRID;
        for (int i = 0; i < commands.length() - 1; i++) {
            int p1 = grid.findAll(commands.charAt(i))[0];
            int p2 = grid.findAll(commands.charAt(i + 1))[0];
            dist += grid.distance(p1, p2);

        }
        return dist;
    }

    private Set<String> moves(int c1, int c2) {
        var move = new Move(c1, c2);
        if (computedMoves.containsKey(move)) {
            return computedMoves.get(move);
        }
        var grid = NUMERIC_GRID.findAll(c1).length > 0 && NUMERIC_GRID.findAll(c2).length > 0 ? NUMERIC_GRID : DIRECTIONAL_GRID;
        int from = grid.findAll(c1)[0];
        int to = grid.findAll(c2)[0];
        int deltaX = grid.getX(to) - grid.getX(from);
        int deltaY = grid.getY(to) - grid.getY(from);
        var set = new HashSet<String>();
        if (grid.getValue(grid.move(from, deltaX, 0)) != ' ') {
            set.add(
                    new StringBuilder()
                            .repeat(deltaX > 0 ? '>' : '<', Math.abs(deltaX))
                            .repeat(deltaY > 0 ? 'v' : '^', Math.abs(deltaY))
                            .append('A')
                            .toString()
            );
        }
        if (grid.getValue(grid.move(from, 0, deltaY)) != ' ') {
            set.add(
                    new StringBuilder()
                            .repeat(deltaY > 0 ? 'v' : '^', Math.abs(deltaY))
                            .repeat(deltaX > 0 ? '>' : '<', Math.abs(deltaX))
                            .append('A')
                            .toString()
            );
        }
        computedMoves.put(move, set);
        System.out.printf("Found moves %s from %c to %c%n", set, c1, c2);

        return set;
    }

    private String reverse(Grid grid, String keyPresses) {
        System.out.printf("keyPresses=%s%n", keyPresses);

        var commandsList = new ArrayList<String>();
        var aPos = grid.findAll('A')[0];

//        for (var keyPresses : keyPressesList) {
        var tails = new HashSet<CommandNode>();
        tails.add(new CommandNode(0, 0, null));

        for (int from = aPos, i = 0; i < keyPresses.length(); i++) {
            int to = grid.findAll(keyPresses.charAt(i))[0];
            int deltaX = grid.getX(to) - grid.getX(from);
            int deltaY = grid.getY(to) - grid.getY(from);
            var newTails = new HashSet<CommandNode>();
            var xNode = (UnaryOperator<CommandNode>) n -> deltaX == 0 ? n :
                    new CommandNode(deltaX > 0 ? '>' : '<', Math.abs(deltaX), n);
            var yNode = (UnaryOperator<CommandNode>) n -> deltaY == 0 ? n :
                    new CommandNode(deltaY > 0 ? 'v' : '^', Math.abs(deltaY), n);
            for (var n : tails) {
                if (grid.getValue(from + deltaX) != ' ') {
                    newTails.add(new CommandNode('A', 1, yNode.apply(xNode.apply(n))));
                }
                if (grid.getValue(from + (deltaY * grid.width())) != ' ') {
                    newTails.add(new CommandNode('A', 1, xNode.apply(yNode.apply(n))));
                }
            }
            tails = newTails;
            from = to;
        }
        commandsList.addAll(
                tails.stream()
                        .map(tail -> Stream.iterate(tail, n -> n.prev() != null, n -> n.prev())
                                .collect(
                                        StringBuilder::new,
                                        (s, n) -> s.repeat(n.command(), n.count()),
                                        (s1, s2) -> s1.append(s2)
                                ))
                        .map(StringBuilder::reverse)
                        .map(StringBuilder::toString)
                        .toList()
        );
        commandsList.sort(Comparator.comparing(c -> distance(c)));
        return commandsList.getFirst();
    }

    record CodeDepth(String code, int depth) {
    }

    record CommandNode(int command, int count, CommandNode prev) {
    }

    record Move(int c1, int c2) {
    }
}
