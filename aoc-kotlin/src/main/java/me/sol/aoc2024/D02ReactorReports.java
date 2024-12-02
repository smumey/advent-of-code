package me.sol.aoc2024;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

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
        try (var reader = new BufferedReader(new InputStreamReader(DO1LocationDistance.class.getResourceAsStream("/input/aoc2024/d02-input")))) {
            System.out.println(new D02ReactorReports(parse(reader)).countSafe());
        }
    }

    static Report[] parse(BufferedReader reader) throws IOException {
        return reader.lines().map(s -> new Report(
                        Arrays.stream(s.split("\\s+")).mapToLong(Long::parseLong).toArray()
                ))
                .toArray(Report[]::new);
    }

    long countSafe() {
        return Arrays.stream(reports).filter(Report::isSafe).count();
    }

    record Report(long[] readings) {
        boolean isSafe() {
            if (readings.length < 2) {
                return true;
            }
            long r1 = readings[0];
            long r2 = readings[1];
            var sign = r2 > r1 ? 1L : -1L;
            if (!checkPair(r1, r2, sign)) {
                return false;
            }
            long previous = r2;
            for (int i = 2; i < readings.length; i++) {
                if (!checkPair(previous, readings[i], sign)) {
                    return false;
                }
                previous = readings[i];
            }
            return true;
        }
    }
}
