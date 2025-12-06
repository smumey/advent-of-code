package me.sol.aoc2025;

import me.sol.Utility;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class D02GiftShopTest {

    @Test
    void sumInvalidP1() throws IOException {
        assertThat(new D02GiftShop(Utility.readSample(
                getClass(),
                D02GiftShop::parse
        )).sumInvalidP1()).isEqualTo(1227775554L);
    }

    @Test
    void sumInvalidP2() throws IOException {
        assertThat(new D02GiftShop(Utility.readSample(
                getClass(),
                D02GiftShop::parse
        )).sumInvalidP2()).isEqualTo(4174379265L);
    }
}
