package me.sol.aoc2025;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record D01SecretEntrance(int startPos, int clicks, List<Integer> rotations) {
    static final int CLICKS = 100;
    static final int START_POS = 50;

    record ClickState(int pos, int zeroes) {
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D01SecretEntrance(
                START_POS,
                CLICKS,
                Utility.readInput(D01SecretEntrance.class, D01SecretEntrance::parse)
        ));
    }

    public static List<Integer> parse(Stream<String> lines) {
        return lines.map(l -> {
            var magnitude = Integer.parseInt(l.substring(1));
            return switch (l.charAt(0)) {
                case 'L' -> -1 * magnitude;
                case 'R' -> magnitude;
                default -> throw new IllegalArgumentException();
            };
        }).toList();
    }

    @Answer
    public long zeroCount() {
        var it = rotations.iterator();
        return IntStream.iterate(START_POS, p -> p >= 0, p -> it.hasNext() ? ((p + it.next()) % clicks + clicks) % clicks : -1)
                .filter(p -> p == 0)
                .count();
    }

    @Answer
    public long zeroClickCount() {
        var it = rotations.iterator();
        return Stream.iterate(
                        new ClickState(startPos, 0),
                        s -> s != null,
                        s -> {
                            if (!it.hasNext()) {
                                return null;
                            }
                            var rot = it.next();
                            var newPos = ((s.pos() + rot) % clicks() + clicks()) % clicks();
                            var delta = rot >= 0 ? rot - (clicks() - s.pos()) : -(s.pos() == 0 ? clicks() : s.pos()) - rot;
                            return new ClickState(
                                    newPos,
                                    delta >= 0 ? 1 + delta / clicks() : 0
                            );
                        }
                )
                .mapToInt(ClickState::zeroes)
                .sum();
    }
}
