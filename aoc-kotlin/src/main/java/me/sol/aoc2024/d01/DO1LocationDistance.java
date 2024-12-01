package me.sol.aoc2024.d01;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DO1LocationDistance {
    public DO1LocationDistance(List<Integer> list1, List<Integer> list2) {

    }

    static Locations parse(InputStream reader) {
        var list1 = new ArrayList<Integer>();
        var list2 = new ArrayList<Integer>();
        try (var scanner = new Scanner(reader)) {
            while (scanner.hasNextInt()) {
                list1.add(scanner.nextInt());
                list2.add(scanner.nextInt());
            }
        }
        return new Locations(list1, list2);
    }

    public static void main(String[] args) throws IOException {
        try (var reader = DO1LocationDistance.class.getResourceAsStream("/input/aoc2024/d01-input")) {
            System.out.println(parse(reader).totalDistance());
        }
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
    }
}