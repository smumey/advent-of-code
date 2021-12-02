package main

import (
	"fmt"
	"io"
)

const (
	north = 0
	east  = 90
	south = 180
	west  = 270
)

type instructionT struct {
	name      rune
	magnitude int
	execute   func(boatT, instructionT) boatT
}

type boatT struct {
	direction int
	x         int
	y         int
}

type waypointT struct {
	x int
	y int
}

func abs(n int) int {
	if n < 0 {
		return -n
	}
	return n
}

func (boat boatT) distance() int {
	return abs(boat.x) + abs(boat.y)
}

func (waypoint waypointT) move(direction int, distance int) waypointT {
	switch direction {
	case north:
		waypoint.y += distance
	case east:
		waypoint.x += distance
	case south:
		waypoint.y -= distance
	case west:
		waypoint.x -= distance
	default:
		panic(fmt.Errorf("invalid direction %d", direction))
	}
	return waypoint
}

func (boat boatT) move(waypoint waypointT, magnitude int) boatT {
	boat.x += magnitude * waypoint.x
	boat.y += magnitude * waypoint.y
	return boat
}

func (waypoint waypointT) rotate(angle int) waypointT {
	x := waypoint.x
	y := waypoint.y
	angle = (angle + 360) % 360
	switch angle {
	case 90:
		waypoint.x = y
		waypoint.y = -x
	case 180:
		waypoint.x = -x
		waypoint.y = -y
	case 270:
		waypoint.x = -y
		waypoint.y = x
	default:
		panic(fmt.Errorf("bad rotate angle %d", angle))
	}
	return waypoint
}

func execute(boat boatT, waypoint waypointT, instruction instructionT) (boatT, waypointT) {
	magnitude := instruction.magnitude
	switch instruction.name {
	case 'N':
		waypoint = waypoint.move(north, magnitude)
	case 'E':
		waypoint = waypoint.move(east, magnitude)
	case 'S':
		waypoint = waypoint.move(south, magnitude)
	case 'W':
		waypoint = waypoint.move(west, magnitude)
	case 'L':
		waypoint = waypoint.rotate(-magnitude)
	case 'R':
		waypoint = waypoint.rotate(magnitude)
	case 'F':
		boat = boat.move(waypoint, magnitude)
	default:
		panic(fmt.Errorf("invalid instruction %c", instruction.name))
	}
	return boat, waypoint
}

func main() {
	initial := boatT{
		90,
		0,
		0,
	}
	current := initial
	waypoint := waypointT{
		10,
		1,
	}
	for {
		var instruction instructionT
		_, err := fmt.Scanf("%c%d", &instruction.name, &instruction.magnitude)
		if err == io.EOF {
			break
		}
		if err != nil {
			panic(err)
		}
		current, waypoint = execute(current, waypoint, instruction)
	}
	fmt.Println(current.distance())
}
