package me.sol.aoc2025;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class D04PrintingDepartmentTest {
    @Test
    void p1RollCount() throws IOException {
        assertThat(new D04PrintingDepartment(Utility.readSample(getClass(), Utility::parseCharGrid)).p1RollCount()).isEqualTo(13);
    }

    @Test
    void p1RemoveCount() throws IOException {
        assertThat(new D04PrintingDepartment(Utility.readSample(getClass(), Utility::parseCharGrid)).p2RemoveCount()).isEqualTo(43);
    }
}
