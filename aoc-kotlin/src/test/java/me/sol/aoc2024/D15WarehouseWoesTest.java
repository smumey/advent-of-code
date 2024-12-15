package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class D15WarehouseWoesTest {

    @Test
    void sumBoxGps() throws IOException {
        assertThat(new D15WarehouseWoes(Utility.readSample(getClass(), D15WarehouseWoes::parse)).sumBoxGps()).isEqualTo(10092L);
    }

    @Test
    void sumBoxGps_smaller() {
        var input = """
                ########
                #..O.O.#
                ##@.O..#
                #...O..#
                #.#.O..#
                #...O..#
                #......#
                ########
                
                <^^>>>vv<v>>v<<
                """;
        assertThat(new D15WarehouseWoes(D15WarehouseWoes.parse(input.lines())).sumBoxGps()).isEqualTo(2028);
    }
}
