package me.sol.aoc2024;

import me.sol.Utility;
import me.sol.aoc2024.D17ChronospatialComputer.PState;
import org.junit.jupiter.api.Disabled;
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
        var state = new PState(10, 0, 0, List.of(5, 0, 5, 1, 5, 4));
        assertThat(new D17ChronospatialComputer(state).output()).isEqualTo("0,1,2");
    }

    //    A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A.
    @Test
    void output_when_A2024AndP015430() {
        var initialState = new PState(2024, 0, 0, List.of(0, 1, 5, 4, 3, 0));
        assertThat(new D17ChronospatialComputer(initialState).output()).isEqualTo("4,2,5,6,7,7,7,7,3,1,0");
    }

    @Test
    void next() {
        var program = List.of(2, 6);
        var newP = new PState(0, 0, 9, program);
        newP.next();
        assertThat(newP.regB).isEqualTo(1);
    }

    //    If register B contains 29, the program 1,7 would set register B to 26
    @Test
    void next2() {
        var program = List.of(1, 7);
        var newP = new PState(0L, 29L, 0L, program);
        newP.next();
        assertThat(newP.regB).isEqualTo(26L);
    }

    //    If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354.    @Test
    void next3() {
        var program = List.of(4, 0);
        var newP = new PState(0L, 2024L, 43960L, program);
        newP.next();
        assertThat(newP.regB).isEqualTo(44354L);
    }

    @Test
    @Disabled
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
