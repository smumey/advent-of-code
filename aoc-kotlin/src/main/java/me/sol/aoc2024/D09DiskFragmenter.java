package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class D09DiskFragmenter {
    private final int[] diskLayout;

    public D09DiskFragmenter(int[] diskLayout) {
        this.diskLayout = diskLayout;
    }

    public static int[] parse(Stream<String> lines) {
        return lines.findFirst().orElseThrow().chars().map(c -> c - '0').toArray();
    }

    public static void main(String[] args) throws IOException {
        Utility.execute(new D09DiskFragmenter(Utility.readInput(D09DiskFragmenter.class, D09DiskFragmenter::parse)));
    }

    private int nextFreeSegmentIndex(int[] blocks, int lastIndex) {
        for (int i = lastIndex + 1; i < blocks.length; i++) {
            if (blocks[i] == -1) {
                return i;
            }
        }
        return blocks.length;
    }

    private int nextFileSegmentIndex(int[] blocks, int lastIndex) {
        for (int i = lastIndex - 1; i >= 0; i--) {
            if (blocks[i] >= 0) {
                return i;
            }
        }
        return -1;
    }

    @Answer
    long p1Checksum() {
        var blocks = IntStream.range(0, diskLayout.length)
                .flatMap(i -> IntStream.generate(() -> i % 2 == 0 ? i / 2 : -1).limit(diskLayout[i]))
                .toArray();
        int freeSegmentIndex = nextFreeSegmentIndex(blocks, -1);
        int fileSegmentIndex = nextFileSegmentIndex(blocks, blocks.length);
        while (freeSegmentIndex < fileSegmentIndex) {
            blocks[freeSegmentIndex] = blocks[fileSegmentIndex];
            blocks[fileSegmentIndex] = -1;
            freeSegmentIndex = nextFreeSegmentIndex(blocks, freeSegmentIndex);
            fileSegmentIndex = nextFileSegmentIndex(blocks, fileSegmentIndex);
        }
        return IntStream.range(0, blocks.length)
                .filter(i -> blocks[i] >= 0)
                .map(i -> i * blocks[i])
                .mapToLong(i -> i)
                .sum();
    }
}
