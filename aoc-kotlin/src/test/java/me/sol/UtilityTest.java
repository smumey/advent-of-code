package me.sol;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UtilityTest {
    @Test
    void primeFactors() {
        assertThat(Utility.primeFactors(2)).isEqualTo(List.of(2));
        assertThat(Utility.primeFactors(600)).isEqualTo(List.of(2, 2, 2, 3, 5, 5));
        assertThat(Utility.primeFactors(1045)).isEqualTo(List.of(5, 11, 19));
        assertThat(Utility.primeFactors(1045L)).isEqualTo(List.of(5L, 11L, 19L));
    }
}
