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

func (boat boatT) offset(waypoint waypointT) {

}

func (boat boatT) move(direction int, distance int) boatT {
	switch direction {
	case north:
		boat.y += distance
	case east:
		boat.x += distance
	case south:
		boat.y -= distance
	case west:
		boat.x -= distance
	default:
		panic(fmt.Errorf("invalid direction %d", direction))
	}
	return boat
}

func (boat boatT) rotate(angle int) boatT {
	boat.direction = (boat.direction + angle + 360) % 360
	return boat
}

func (boat boatT) execute(instruction instructionT) boatT {
	magnitude := instruction.magnitude
	switch instruction.name {
	case 'N':
		return boat.move(north, magnitude)
	case 'E':
		return boat.move(east, magnitude)
	case 'S':
		return boat.move(south, magnitude)
	case 'W':
		return boat.move(west, magnitude)
	case 'L':
		return boat.rotate(-magnitude)
	case 'R':
		return boat.rotate(magnitude)
	case 'F':
		return boat.move(boat.direction, magnitude)
	default:
		panic(fmt.Errorf("invalid instruction %c", instruction.name))
	}
}

func main() {
	initial := boatT{
		90,
		0,
		0,
	}
	current := initial
	for {
		var instruction instructionT
		_, err := fmt.Scanf("%c%d", &instruction.name, &instruction.magnitude)
		if err == io.EOF {
			break
		}
		if err != nil {
			panic(err)
		}
		current = current.execute(instruction)
	}
	fmt.Println(current.distance())
}
