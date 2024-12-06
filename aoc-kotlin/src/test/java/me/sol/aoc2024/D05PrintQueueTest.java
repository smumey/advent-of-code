package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D05PrintQueueTest {
    @Test
    void p1correctlyOrderedMiddlePageSum() throws IOException {
        assertThat(new D05PrintQueue(Utility.readSample(D05PrintQueue.class, D05PrintQueue::parse)).p1correctlyOrderedMiddlePageSum()).isEqualTo(143);
    }

    @Test
    void p2reorderedMiddlePageSum() throws IOException {
        assertThat(new D05PrintQueue(Utility.readSample(D05PrintQueue.class, D05PrintQueue::parse)).p2reorderedMiddlePageSum()).isEqualTo(123);
    }
}