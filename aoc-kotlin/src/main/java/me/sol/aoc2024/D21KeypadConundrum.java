package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Grid;
import me.sol.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
    private final List<String> codes;

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
    int complexitySum() {
        var commandsLists = codes.stream()
                .map(code -> reverse(DIRECTIONAL_GRID, reverse(DIRECTIONAL_GRID, reverse(NUMERIC_GRID, List.of(code)))))
                .toList();
        System.out.println(commandsLists);
        var bestCommands = commandsLists.stream()
                .map(commands -> commands.stream().sorted(Comparator.comparing(String::length)).findFirst().orElseThrow())
                .toList();
        System.out.printf("bestCommands=%s%n", bestCommands);
        return IntStream.range(0, bestCommands.size())
                .map(i -> bestCommands.get(i).length() * Integer.parseInt(codes.get(i).substring(0, codes.get(i).length() - 1)))
                .sum();
    }

    private List<String> reverse(Grid grid, List<String> keyPressesList) {
        System.out.printf("keyPresses=%s%n", keyPressesList);

        var commandsList = new ArrayList<String>();
        var aPos = grid.findAll('A')[0];

        for (var keyPresses : keyPressesList) {
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
                                    .collect(StringBuilder::new, (s, n) -> s.repeat(n.command(), n.count()), (s1, s2) -> s1.append(s2)))
                            .map(StringBuilder::reverse)
                            .map(StringBuilder::toString)
                            .toList()
            );
        }
        return commandsList;
    }

    record CommandNode(int command, int count, CommandNode prev) {
    }
}
