package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class D13ClawContraption {
    public static final long P2_DELTA = 10000000000000L;
    private final List<ClawMachine> machines;

    D13ClawContraption(List<ClawMachine> machines) {
        this.machines = machines;
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D13ClawContraption(Utility.readInput(D13ClawContraption.class, D13ClawContraption::parse)));
    }

    private static long[] read(String l) {
        System.out.println(l);
        return Arrays.stream(l.split("\\D+")).filter(not(String::isEmpty)).mapToLong(Long::parseLong).toArray();
    }

    static List<ClawMachine> parse(Stream<String> lines) {
        List<String> lineList = lines.toList();
        var it = lineList.iterator();
        var machines = new ArrayList<ClawMachine>(lineList.size() / 4);
        while (it.hasNext()) {
            var next = it.next();
            if (next.isEmpty()) {
                continue;
            }
            var aVals = read(next);
            var bVals = read(it.next());
            var pVals = read(it.next());
            machines.add(new ClawMachine(aVals[0], aVals[1], bVals[0], bVals[1], pVals[0], pVals[1]));
        }
        return Collections.unmodifiableList(machines);
    }

    @Answer
    long p1TokenCount() {
        return machines.stream().mapToLong(ClawMachine::tokens).sum();
    }

    @Answer
    long p2TokenCount() {
        return machines.stream()
                .map(m -> new ClawMachine(m.aX, m.aY, m.bX, m.bY, m.pX + P2_DELTA, m.pY + P2_DELTA))
                .mapToLong(ClawMachine::tokens)
                .sum();
    }

    record ClawMachine(long aX, long aY, long bX, long bY, long pX, long pY) {
        long tokens() {
            long det = aX * bY - aY * bX;
            if (det == 0) {
                if (aX > 3 * bX) {
                    for (long a = pX / aX /* Long.min(pX / aX, 100L) */; a >= 0; a--) {
                        if ((pX - a * aX) % bX != 0) {
                            continue;
                        }
                        if ((pY - a * aY) % bY != 0) {
                            continue;
                        }
                        long b = (pX - a * aX) / bX;
                        if (b > 100L) {
                            continue;
                        }
                        return 3 * a + b;
                    }
                } else {
                    for (long a = 0; a <= pX / aX /* Long.min(pX / aX, 100L) */; a++) {
                        if ((pX - a * aX) % bX != 0) {
                            continue;
                        }
                        if ((pY - a * aY) % bY != 0) {
                            continue;
                        }
                        long b = (pX - a * aX) / bX;
                        if (b > 100L) {
                            continue;
                        }
                        return 3 * a + b;
                    }
                }
                return 0;
            } else {
                long dA = bX * pY - bY * pX;
                long dB = aY * pX - aX * pY;
                if (dA % det != 0L || dB % det != 0L) {
                    return 0;
                }
                long a = -dA / det;
                long b = -dB / det;
                return 3 * a + b; // a <= 100 && b <= 100 ? 3 * a + b : 0L;
            }
        }

    }
}
