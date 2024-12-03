package me.sol.aoc2024;

import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.stream.Stream;

public class D02ReactorReports {
    private final Report[] reports;

    public D02ReactorReports(Report[] reports) {
        this.reports = reports;
    }

    private static boolean checkPair(long x, long y, long sign) {
        long step = (y - x) * sign;
        return 0 < step && step < 4;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new D02ReactorReports(Utility.readInput(D02ReactorReports.class, D02ReactorReports::parse)).countSafeDampened());
    }

    static Report[] parse(Stream<String> lines) {
        return lines.map(s -> new Report(
                        Arrays.stream(s.split("\\s+")).mapToLong(Long::parseLong).toArray()
                ))
                .toArray(Report[]::new);
    }

    long countSafe() {
        return Arrays.stream(reports).filter(Report::isSafe).count();
    }

    long countSafeDampened() {
        return Arrays.stream(reports).filter(Report::isSafeDampened).count();
    }

    record Report(long[] readings) {
        boolean isSafe() {
            return isSafe(-1);
        }

        boolean isSafe(int skip) {
            if (readings.length < 2 || skip >= 0 && readings.length < 3) {
                return true;
            }
            var nextIndex = (IntUnaryOperator) i -> i == skip ? i + 2 : i + 1;
            var i0 = nextIndex.applyAsInt(-1);
            var i1 = nextIndex.applyAsInt(i0);
            long r0 = readings[i0];
            long r1 = readings[i1];
            var sign = r1 > r0 ? 1L : -1L;
            if (!checkPair(r0, r1, sign)) {
                return false;
            }
            long previous = r1;
            for (int i = nextIndex.applyAsInt(i1); i < readings.length; i = nextIndex.applyAsInt(i)) {
                if (!checkPair(previous, readings[i], sign)) {
                    return false;
                }
                previous = readings[i];
            }
            return true;
        }

        boolean isSafeDampened() {
            if (isSafe()) {
                return true;
            }
            for (int i = 0; i < readings.length; i++) {
                if (isSafe(i)) {
                    return true;
                }
            }
            return false;
        }
    }

}
