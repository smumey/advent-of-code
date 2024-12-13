package me.sol.aoc2024;

import me.sol.Utility;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class D12GardenGroupsTest {
    private static Stream<Arguments> p2TestMaps() {
        return Stream.of(
                Arguments.of(
                        """
                                AAAAAA
                                AAABBA
                                AAABBA
                                ABBAAA
                                ABBAAA
                                AAAAAA
                                """,
                        368
                ),
                Arguments.of(
                        """
                                EEEEE
                                EXXXX
                                EEEEE
                                EXXXX
                                EEEEE
                                """,
                        236
                )
        );
    }

    @Test
    @Disabled
    void p1Cost() throws IOException {
        assertThat(new D12GardenGroups(Utility.readSample(getClass(), Utility::parseCharGrid)).p1Cost()).isEqualTo(1930);
    }

    @Test
    @Disabled
    void p2Cost() throws IOException {
        assertThat(new D12GardenGroups(Utility.readSample(getClass(), Utility::parseCharGrid)).p2Cost()).isEqualTo(1206);
    }

    @MethodSource("p2TestMaps")
    @ParameterizedTest
    void p2Cost(String map, int cost) {
        var grid = Utility.parseCharGrid(Arrays.stream(map.split("\\n")));
        assertThat(new D12GardenGroups(grid).p2Cost()).isEqualTo(cost);
    }

}
