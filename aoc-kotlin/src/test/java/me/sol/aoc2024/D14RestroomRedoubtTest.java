package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D14RestroomRedoubtTest {

    @Test
    void safetyFactor() throws IOException {
        assertThat(new D14RestroomRedoubt(Utility.readSample(getClass(), D14RestroomRedoubt::parse), 11, 7).p1SafetyFactor())
                .isEqualTo(12);
    }
}
