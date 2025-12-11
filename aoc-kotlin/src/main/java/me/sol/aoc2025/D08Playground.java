package me.sol.aoc2025;

import aoc.Coordinate3D;
import me.sol.Answer;

import java.util.List;

public record D08Playground(List<Coordinate3D> coordinates) {
    record Coordinate(long x, long y, long z) {
        long squaredDistance(Coordinate other) {
            var deltaX = other.x - x;
            var deltaY = other.y - y;
            var deltaZ = other.z - z;
            return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
        }
    }

    @Answer

}
