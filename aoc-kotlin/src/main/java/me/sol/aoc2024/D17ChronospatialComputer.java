package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class D17ChronospatialComputer {
    private final PState initialState;

    public static void main(String[] args) throws IOException {
        Utility.execute(new D17ChronospatialComputer(Utility.readInput(D17ChronospatialComputer.class, D17ChronospatialComputer::parse)));
    }

    static PState parse(Stream<String> lineStream) {
        var lines = lineStream.toList();
        int a = Utility.parseInts(lines.get(0))[0];
        return new PState(
                a,
                a,
                Utility.parseInts(lines.get(1))[0],
                Utility.parseInts(lines.get(2))[0],
                Arrays.stream(Utility.parseInts(lines.get(4))).boxed().toList(),
                0,
                0,
                List.of()
        );
    }

    D17ChronospatialComputer(PState initialState) {
        this.initialState = initialState;
    }

    @Answer
    String output() {
        var finalState = Stream.iterate(initialState, Objects::nonNull, PState::next)
                .reduce((s1, s2) -> s2)
                .orElseThrow();
        return finalState.output().stream().map(Object::toString).collect(joining(","));
    }

    @Answer
    int lowestValueToDuplicate() {
        return IntStream.iterate(1, a -> a + 1)
                .mapToObj(a -> new PState(a, a, initialState.regB(), initialState.regC(), initialState.instrucs(), 0, 0, List.of()))
                .flatMap(s -> Stream.iterate(s, t -> t != null && t.output().equals(t.instrucs().subList(0, t.output().size())), PState::next))
                .filter(s -> s.output().equals(s.instrucs()))
                .mapToInt(PState::initialA)
                .findFirst()
                .orElseThrow();
    }

    enum Operation implements UnaryOperator<PState> {
        ADV {
            @Override
            public PState apply(PState p) {
                return new PState(p, p.regA() / (1 << p.combo()), p.regB(), p.regC(), p.pointer() + 2, p.output());
            }
        },
        BXL {
            @Override
            public PState apply(PState p) {
                return new PState(p, p.regA(), p.regB() ^ p.literal(), p.regC(), p.pointer() + 2, p.output());
            }
        },
        BST {
            @Override
            public PState apply(PState p) {
                return new PState(p, p.regA(), p.combo() % 8, p.regC(), p.pointer() + 2, p.output());
            }
        },
        JNZ {
            @Override
            public PState apply(PState p) {
                var pointer = p.regA() != 0 && p.literal() != p.pointer() ? p.literal() : p.pointer() + 2;
                return new PState(p, p.regA(), p.regB(), p.regC(), pointer, p.output());
            }
        },
        BXC {
            @Override
            public PState apply(PState p) {
                return new PState(p, p.regA(), p.regB() ^ p.regC(), p.regC(), p.pointer() + 2, p.output());
            }
        },
        OUT {
            @Override
            public PState apply(PState p) {
                return new PState(p, p.regA(), p.regB(), p.regC(), p.pointer() + 2, concat(p.output(), p.combo() % 8));
            }
        },
        BDV {
            @Override
            public PState apply(PState p) {
                return new PState(p, p.regA(), p.regA() / (1 << p.combo()), p.regC(), p.pointer() + 2, p.output());
            }
        },
        CDV {
            @Override
            public PState apply(PState p) {
                return new PState(p, p.regA(), p.regB(), p.regA() / (1 << p.combo()), p.pointer() + 2, p.output());
            }
        };

        private static List<Integer> concat(List<Integer> list, int add) {
            var newList = new ArrayList<Integer>(list.size() + 1);
            newList.addAll(list);
            newList.add(add);
            return Collections.unmodifiableList(newList);
        }
    }

    record PState(
            int initialA,
            int regA,
            int regB,
            int regC,
            List<Integer> instrucs,
            int pointer,
            int step,
            List<Integer> output
    ) {
        PState(PState other, int regA, int regB, int regC, int pointer, List<Integer> output) {
            this(other.initialA(), regA, regB, regC, other.instrucs(), pointer, other.step() + 1, output);
        }

        int combo() {
            var operand = instrucs.get(pointer + 1);
            return switch (operand) {
                case 4 -> regA;
                case 5 -> regB;
                case 6 -> regC;
                case 7 -> throw new IllegalStateException("combo operand 7 is reserved");
                default -> operand;
            };
        }

        boolean halt() {
            return pointer >= instrucs.size();
        }

        PState next() {
            if (halt()) {
                return null;
            }
            var operation = Operation.values()[instrucs.get(pointer)];
            PState n = operation.apply(this);
            return n;
        }

        int literal() {
            return instrucs.get(pointer + 1);
        }
    }
}
