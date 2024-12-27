package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D24CrossedWiresTest {

    @Test
    void p1ZedValue() throws IOException {
        assertThat(new D24CrossedWires(Utility.readSample(getClass(), D24CrossedWires::parse), 0).p1ZedValue())
                .isEqualTo(2024L);
    }

//    @Test
//    void p2SwappedOutputWires() {
//        var input = """
//                x00: 0
//                x01: 1
//                x02: 0
//                x03: 1
//                x04: 0
//                x05: 1
//                y00: 0
//                y01: 0
//                y02: 1
//                y03: 1
//                y04: 0
//                y05: 1
//
//                x00 AND y00 -> z05
//                x01 AND y01 -> z02
//                x02 AND y02 -> z01
//                x03 AND y03 -> z03
//                x04 AND y04 -> z04
//                x05 AND y05 -> z00
//                """;
//        assertThat(new D24CrossedWires(D24CrossedWires.parse(input.lines()), 2).p2SwappedOutputWires())
//                .isEqualTo("z00,z01,z02,z05");
//    }
}
