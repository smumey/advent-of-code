package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D05PrintQueueTest {
    @Test
    void pageSum() throws IOException {
        assertThat(new D05PrintQueue(Utility.readSample(D05PrintQueue.class, D05PrintQueue::parse)).middlePageSum()).isEqualTo(143);
    }

}