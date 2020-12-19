package main

import (
	"errors"
	"fmt"
	"io"
)

type instructT = struct {
	operation string
	argument  int
	count     int
}

var errRepeat error = errors.New("repeated operation")
var errNoMore = errors.New("no more mutations")

type outOfBoundsError int

func (err outOfBoundsError) Error() string {
	return fmt.Sprintf("Pointer %d out of range.", err)
}

type unsupportedOpError string

func (err unsupportedOpError) Error() string {
	return fmt.Sprintf("Operation %s is not supported", err)
}

func process(instructions []instructT, pointer int, accumulator int) (int, error) {
	if pointer == len(instructions) {
		return accumulator, nil
	} else if pointer < 0 || pointer > len(instructions) {
		return 0, outOfBoundsError(pointer)
	}
	instruction := &instructions[pointer]
	if instruction.count > 0 {
		return 0, errRepeat
	}
	instruction.count++
	switch instruction.operation {
	case "nop":
		return process(instructions, pointer+1, accumulator)
	case "acc":
		return process(instructions, pointer+1, accumulator+instruction.argument)
	case "jmp":
		return process(instructions, pointer+instruction.argument, accumulator)
	default:
		return 0, unsupportedOpError(instruction.operation)
	}
}

func isSwappable(instruction instructT) bool {
	return instruction.operation == "nop" || instruction.operation == "jmp"
}

func swap(instruction instructT) instructT {
	switch instruction.operation {
	case "nop":
		instruction.operation = "jmp"
	case "jmp":
		instruction.operation = "nop"
	}
	return instruction
}

func mutate(instructions []instructT) func() ([]instructT, error) {
	m := 0
	return func() ([]instructT, error) {
		mut := make([]instructT, len(instructions))
		n := 0
		for i, instruction := range instructions {
			newI := instruction
			if n < m && isSwappable(instruction) {
				if n == m-1 {
					newI = swap(instruction)
				}
				n++
			}
			mut[i] = newI
		}
		if n == m {
			m++
			return mut, nil
		}
		return nil, errNoMore
	}
}

func main() {
	var instructions []instructT
	for {
		instruction := instructT{}
		n, err := fmt.Scanf("%s %d", &instruction.operation, &instruction.argument)
		fmt.Printf("n=%d err=%v\n", n, err)
		if err == io.EOF {
			break
		} else if err != nil {
			panic(err)
		}
		instructions = append(instructions, instruction)
	}
	mutator := mutate(instructions)
	for {
		newI, err := mutator()
		if err != nil {
			panic(err)
		}
		accumulator, err := process(newI, 0, 0)
		if err != nil {
			fmt.Printf("Instructions failed with %v.\n", err)
		} else {
			fmt.Println(accumulator)
			break
		}

	}
}
