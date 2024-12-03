package me.sol.aoc2024;

import me.sol.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class D01LocationDistance {
    static Locations parse(Stream<String> lines) {
        var list1 = new ArrayList<Integer>();
        var list2 = new ArrayList<Integer>();
        lines.map(l -> new Scanner(l)).forEach(s -> {
            list1.add(s.nextInt());
            list2.add(s.nextInt());
        });
        return new Locations(list1, list2);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(
                Utility.readInput(D01LocationDistance.class, D01LocationDistance::parse).similarity()
        );
    }

    record Locations(List<Integer> list1, List<Integer> list2) {
        Locations {
            list1 = List.copyOf(list1);
            list2 = List.copyOf(list2);
        }

        int totalDistance() {
            var l1 = new ArrayList<>(list1);
            var l2 = new ArrayList<>(list2);
            l1.sort(Integer::compareTo);
            l2.sort(Integer::compareTo);
            var d = 0;
            for (var i = 0; i < l1.size(); i++) {
                d += Math.abs(l1.get(i) - l2.get(i));
            }
            return d;
        }

        int similarity() {
            var l1 = new ArrayList<>(list1);
            var l2 = new ArrayList<>(list2);
            l1.sort(Integer::compareTo);
            l2.sort(Integer::compareTo);
            int s = 0;
            for (int i = 0, j = 0, lastLoc = Integer.MIN_VALUE, lastSim = 0; i < l1.size(); i++) {
                int loc = l1.get(i);
                var sim = 0;
                if (loc == lastLoc) {
                    sim = lastSim;
                } else {
                    for (; j < l2.size() && l2.get(j) < loc; j++) {
                    }
                    int count = 0;
                    for (; j < l2.size() && l2.get(j) == loc; j++) {
                        count += 1;
                    }
                    sim = loc * count;
                }
                s += sim;
                lastLoc = loc;
                lastSim = sim;
            }
            return s;
        }
    }
}
