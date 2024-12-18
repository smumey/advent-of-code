package me.sol.aoc2024;

import me.sol.Utility;
import me.sol.aoc2024.D17ChronospatialComputer.PState;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class D17ChronospatialComputerTest {

    @Test
    void output() throws IOException {
        var initialState = Utility.readSample(getClass(), D17ChronospatialComputer::parse);
        assertThat(new D17ChronospatialComputer(initialState).output()).isEqualTo("4,6,3,5,6,3,5,2,1,0");
    }

    @Test
    void output_when_A10AndP505154() {
        var initialState = new PState(10, 10, 0, 0, List.of(5, 0, 5, 1, 5, 4), 0, 0, List.of());
        assertThat(new D17ChronospatialComputer(initialState).output()).isEqualTo("0,1,2");
    }

    //    A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A.
    @Test
    void output_when_A2024AndP015430() {
        var initialState = new PState(2024, 2024, 0, 0, List.of(0, 1, 5, 4, 3, 0), 0, 0, List.of());
        assertThat(new D17ChronospatialComputer(initialState).output()).isEqualTo("4,2,5,6,7,7,7,7,3,1,0");
    }

    @Test
    void next() {
        var program = List.of(2, 6);
        assertThat(new PState(0, 0, 0, 9, program, 0, 0, List.of()).next())
                .isEqualTo(new PState(0, 0, 1, 9, program, 2, 1, List.of()));
    }

    @Test
    void lowestValueToDuplicate() {
        var initialState = D17ChronospatialComputer.parse("""
                Register A: 2024
                Register B: 0
                Register C: 0
                
                Program: 0,3,5,4,3,0
                """.lines());
        assertThat(new D17ChronospatialComputer(initialState).lowestValueToDuplicate()).isEqualTo(117440);
    }
}
