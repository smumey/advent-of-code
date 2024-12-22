package me.sol.aoc2024;

import aoc.Direction;
import me.sol.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
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

    static List<Integer> parse(Stream<String> lineStream) {
        return lineStream.map(l -> Integer.parseInt(l.substring(0,l.length()-1)))
                .toList();
    }

    D21KeypadConundrum(List<String> codes) {
        this.codes = codes;
    }


    class NumericKeypad implements IntConsumer {
        private List<StringBuilder> commands;

        public NumericKeypad(List<String> commands) {
            this.commands = new ArrayList<>(5);
            commands.add(new StringBuilder())
        }

        @Override
        public void accept(int value) {
            if()
        }
    }

    class Robot implements IntConsumer {
        private final Grid grid;
        private int pos;
        private final Consumer<Integer> target;

        Robot(Grid grid, Consumer<Integer> target) {
            this.grid = grid;
            this.target = target;
            pos = grid.findAll('A')[0];
        }

        @Override
        public void accept(int command) {
            var d = switch (command) {
                case '^' -> Optional.of(Direction.UP);
                case '>' -> Optional.of(Direction.RIGHT);
                case '<' -> Optional.of(Direction.LEFT);
                case 'v' -> Optional.of(Direction.DOWN);
                default -> Optional.<Direction>empty();
            };
            d.ifPresent(dir -> {
                var newPos = grid.move(pos, dir);
                pos = newPos == Grid.OUT ? pos : newPos;
            });
            if (command == 'A') {
                target.accept((int) grid.getValue(pos));
            }
        }
    }

}
