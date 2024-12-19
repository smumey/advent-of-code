package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class D17ChronospatialComputer {
    private final PState initialState;

    public static void main(String[] args) throws IOException {
        Utility.execute(new D17ChronospatialComputer(Utility.readInput(D17ChronospatialComputer.class, D17ChronospatialComputer::parse)));
    }

    static PState parse(Stream<String> lineStream) {
        var lines = lineStream.toList();
        return new PState(
                Utility.parseInts(lines.get(0))[0],
                Utility.parseInts(lines.get(1))[0],
                Utility.parseInts(lines.get(2))[0],
                Arrays.stream(Utility.parseInts(lines.get(4))).boxed().toList()
        );
    }

    D17ChronospatialComputer(PState initialState) {
        this.initialState = initialState;
    }

    @Answer
    String output() {
        var state = new PState(initialState, initialState.regA);
        do {
            state.next();
        } while (!state.halt());
        return state.output.stream().map(Object::toString).collect(joining(","));
    }

    @Answer
    long lowestValueToDuplicate() {
        var candidates = new LinkedList<Long>();
        candidates.add(0L);
        for (int i = initialState.instrucs.size() - 1; i >= 0; i--) {
            var newCandidates = new LinkedList<Long>();
            for (long candidate : candidates) {
                for (var add = 0L; add < 8L; add++) {
                    var testA = (candidate >> 3 * i) + add;
                    var state = new PState(testA, initialState.regB, initialState.regC, initialState.instrucs);
                    do {
                        state.next();
                    } while (!state.halt());
                    if (state.output.equals(state.instrucs.subList(i, state.instrucs.size()))) {
                        newCandidates.add(candidate + (add << (3 * i)));
                    }
                }
            }
            candidates = newCandidates;
        }
        candidates.sort(Comparator.naturalOrder());
        System.out.println(candidates);
        long a = candidates.getFirst();
        var testState = new PState(a, initialState.regB, initialState.regC, initialState.instrucs);
        do {
            testState.next();
        } while (!testState.halt());
        if (!testState.output.equals(testState.instrucs)) {
            throw new IllegalStateException("Output is %s not %s".formatted(testState.output, testState.instrucs));
        }
        return a;

    }

    enum Operation implements Consumer<PState> {
        ADV {
            @Override
            public void accept(PState p) {
                p.regA = p.regA >> p.combo();
                p.pointer += 2;
            }
        },
        BXL {
            @Override
            public void accept(PState p) {
                p.regB = p.regB ^ p.literal();
                p.pointer += 2;
            }
        },
        BST {
            @Override
            public void accept(PState p) {
                p.regB = p.combo() & 0x7L;
                p.pointer += 2;
            }
        },
        JNZ {
            @Override
            public void accept(PState p) {
                int literal = p.literal();
                p.pointer = p.regA != 0 && literal != p.pointer ? literal : p.pointer + 2;
            }
        },
        BXC {
            @Override
            public void accept(PState p) {
                p.regB = p.regB ^ p.regC;
                p.pointer += 2;
            }
        },
        OUT {
            @Override
            public void accept(PState p) {
                p.addOutput((int) (p.combo() & 0x7L));
                p.pointer += 2;
            }
        },
        BDV {
            @Override
            public void accept(PState p) {
                p.regB = p.regA >> p.combo();
                p.pointer += 2;
            }
        },
        CDV {
            @Override
            public void accept(PState p) {
                p.regC = p.regA >> p.combo();
                p.pointer += 2;
            }
        }
    }

    static final class PState {
        final long initialA;
        final List<Integer> instrucs;
        final List<Integer> output = new ArrayList<>();
        long regA;
        long regB;
        long regC;
        int pointer = 0;
        long step = 0L;

        PState(
                long regA,
                long regB,
                long regC,
                List<Integer> instrucs
        ) {
            this.initialA = regA;
            this.regA = regA;
            this.regB = regB;
            this.regC = regC;
            this.instrucs = List.copyOf(instrucs);
        }

        PState(PState other, long a) {
            initialA = a;
            instrucs = other.instrucs;
            regA = a;
            regB = other.regB;
            regC = other.regC;
        }

        public void addOutput(int output) {
            this.output.add(output);
        }

        long combo() {
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

        void next() {
            var operation = Operation.values()[instrucs.get(pointer)];
            operation.accept(this);
            this.step += 1;
        }

        int literal() {
            return instrucs.get(pointer + 1);
        }

        @Override
        public String toString() {
            return "PState[" +
                    "initialA=" + initialA + ", " +
                    "regA=" + regA + ", " +
                    "regB=" + regB + ", " +
                    "regC=" + regC + ", " +
                    "instrucs=" + instrucs + ", " +
                    "pointer=" + pointer + ", " +
                    "step=" + step + ", " +
                    "output=" + output +
                    ']';
        }

    }
}
