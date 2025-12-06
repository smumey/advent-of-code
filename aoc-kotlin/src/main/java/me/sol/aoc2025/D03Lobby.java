package me.sol.aoc2025;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public record D03Lobby(List<List<Integer>> bankJoltages) {

    private static final int P2_BATTERIES_ON = 12;

    public static List<List<Integer>> parse(Stream<String> lines) {
        return lines.map(l -> Arrays.stream(l.split("")).map(Integer::parseInt).toList()).toList();
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D03Lobby(Utility.readInput(D03Lobby.class, D03Lobby::parse)));
    }

    private static int p1MaxBankJoltage(List<Integer> batteries) {
        int maxB1 = 0, maxB2 = 0;
        for (var i = 0; i < batteries.size(); i++) {
            var battery = batteries.get(i);
            if (i < batteries.size() - 1 && battery > maxB1) {
                maxB1 = battery;
                maxB2 = 0;
            } else if (battery > maxB2) {
                maxB2 = battery;
            }
        }
        return maxB1 * 10 + maxB2;
    }

    private static long p2MaxBankJoltage(List<Integer> batteries) {
        var on = new int[P2_BATTERIES_ON];
        var bankSize = batteries.size();
        var setBatteries = 0;
        for (var i = 0; i < batteries.size(); i++) {
            int joltage = batteries.get(i);
            for (var j = Math.max(0, i - bankSize + P2_BATTERIES_ON); j < P2_BATTERIES_ON; j++) {
                if (j >= setBatteries || on[j] < joltage) {
                    on[j] = joltage;
                    setBatteries = j + 1;
                    break;
                }
            }
        }
        var total = 0L;
        for (var joltage : on) {
            total = 10L * total + joltage;
        }
        return total;
    }

    @Answer
    public int p1MaxVoltageSum() {
        var max = 0;
        for (var bank : bankJoltages) {
            max += p1MaxBankJoltage(bank);
        }
        return max;
    }

    @Answer
    public long p2MaxVoltageSum() {
        var max = 0L;
        for (var bank : bankJoltages) {
            max += p2MaxBankJoltage(bank);
        }
        return max;
    }
}
