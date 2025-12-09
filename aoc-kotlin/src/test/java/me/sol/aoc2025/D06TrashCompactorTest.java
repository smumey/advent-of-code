package me.sol.aoc2025;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class D06TrashCompactorTest {

    @Test
    void p1Sum() throws IOException {
        assertThat(Utility.readSample(getClass(), D06TrashCompactor::parse).p1Sum()).isEqualTo(4277556L);
    }

    @Test
    void p2Sum() throws IOException {
        assertThat(Utility.readSample(getClass(), D06TrashCompactor::parse).p2Sum()).isEqualTo(3263827L);
    }
}
