package me.sol.aoc2024;

import me.sol.Answer;
import me.sol.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

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
        var blocks = makeBlocks();
        int freeSegmentIndex = nextFreeSegmentIndex(blocks, -1);
        int fileSegmentIndex = nextFileSegmentIndex(blocks, blocks.length);
        while (freeSegmentIndex < fileSegmentIndex) {
            blocks[freeSegmentIndex] = blocks[fileSegmentIndex];
            blocks[fileSegmentIndex] = -1;
            freeSegmentIndex = nextFreeSegmentIndex(blocks, freeSegmentIndex);
            fileSegmentIndex = nextFileSegmentIndex(blocks, fileSegmentIndex);
        }
        return calcChecksum(blocks);
    }

    private static long calcChecksum(int[] blocks) {
        return IntStream.range(0, blocks.length)
                .filter(i -> blocks[i] >= 0)
                .map(i -> i * blocks[i])
                .mapToLong(i -> i)
                .sum();
    }

    @Answer
    long p2Checksum() {
        var blocks = makeBlocks();
        var spaceQueues = Stream.generate(() -> new PriorityQueue<>(comparing(Allocation::index))).limit(10).toList();
        var fileList = new ArrayList<Allocation>(diskLayout.length / 2 + 1);
        for (int i = 0, offset = 0; i < diskLayout.length; i++) {
            if (diskLayout[i] > 0) {
                if (i % 2 == 0) {
                    var file = new Allocation(i / 2, offset, diskLayout[i]);
                    fileList.add(file);
                } else {
                    var space = new Allocation(-1, offset, diskLayout[i]);
                    for (int size = 1; size <= diskLayout[i]; size++) {
                        spaceQueues.get(size).add(space);
                    }
                }
                offset += diskLayout[i];
            }
        }
        for (var file : fileList.reversed()) {
            var spaceQueue = spaceQueues.get(file.size());
            if (spaceQueue.isEmpty()) {
                continue;
            }
            var space = spaceQueue.poll();
            if (file.index() < space.index()) {
                spaceQueue.add(space);
                continue;
            }
            for (int s = 1; s <= space.size(); s++) {
                spaceQueues.get(s).remove(space);
            }
            for (int i = 0; i < file.size(); i++) {
                blocks[space.index() + i] = file.id();
                blocks[file.index() + i] = -1;
            }
            var remaining = space.size() - file.size();
            if (remaining > 0) {
                var remainingSpace = new Allocation(-1, space.index() + file.size(), remaining);
                for (int s = 1; s <= remaining; s++) {
                    spaceQueues.get(s).add(remainingSpace);
                }
            }
        }
        return calcChecksum(blocks);
    }

    private int[] makeBlocks() {
        return IntStream.range(0, diskLayout.length)
                .flatMap(i -> IntStream.generate(() -> i % 2 == 0 ? i / 2 : -1).limit(diskLayout[i]))
                .toArray();
    }

    record Allocation(int id, int index, int size) {
    }
}
