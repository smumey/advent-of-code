package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class D15WarehouseWoesTest {

    public static Stream<Arguments> p2Maps() {
        return Stream.of(
                Arguments.of(
                        """
                                ##########
                                #........#
                                #[][][][]#
                                #.[][][].#
                                #..[][]..#
                                #...[]...#
                                #...[]...#
                                #...@....#
                                ##########
                                
                                ^
                                """,
                        """
                                ##########
                                #[][][][]#
                                #.[][][].#
                                #..[][]..#
                                #...[]...#
                                #...[]...#
                                #...@....#
                                #........#
                                ##########
                                """
                )
        );
    }

    @Test
    void p1SumBoxGps() throws IOException {
        assertThat(new D15WarehouseWoes(Utility.readSample(getClass(), D15WarehouseWoes::parse)).p1SumBoxGps()).isEqualTo(10092L);
    }

    @Test
    void p1SumBoxGps_smaller() {
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
        assertThat(new D15WarehouseWoes(D15WarehouseWoes.parse(input.lines())).p1SumBoxGps()).isEqualTo(2028L);
    }

    @Test
    void p2SumDoubleWideBoxGps() throws IOException {
        assertThat(new D15WarehouseWoes(Utility.readSample(getClass(), D15WarehouseWoes::parse)).p2SumDoubleWideBoxGps()).isEqualTo(9021L);
    }

    @Test
    void p2SumDoubleWideBoxGps_smaller() {
        var input = """
                #######
                #...#.#
                #.....#
                #..OO@#
                #..O..#
                #.....#
                #######
                
                <vv<<^^<<^^
                """;
        assertThat(new D15WarehouseWoes(D15WarehouseWoes.parse(input.lines())).p2SumDoubleWideBoxGps()).isEqualTo(618L);
    }

    @ParameterizedTest
    @MethodSource("p2Maps")
    void nextStateWide(String input, String expectedText) throws IOException {
        var state = D15WarehouseWoes.parse(input.lines());
        try (
                var writer = new StringWriter();
                var bufferedWriter = new BufferedWriter(writer)
        ) {
            var lastState = Stream.iterate(state, Objects::nonNull, s -> s.nextStateWide())
                    .reduce((s1, s2) -> s2).orElseThrow();
            lastState.grid().printAsChars(bufferedWriter);
            bufferedWriter.flush();
            var finalMap = writer.toString();
            assertThat(finalMap).isEqualTo(expectedText);
        }
    }
}
