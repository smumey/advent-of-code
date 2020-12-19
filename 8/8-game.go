package main

import (
	"fmt"
	"io"
)

type instructT = struct {
	operation string
	argument  int
	count     int
}

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
		return accumulator, nil
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
	accumulator, err := process(instructions, 0, 0)
	if err != nil {
		fmt.Printf("Process errored with %v.\n", err)
	} else {
		fmt.Println(accumulator)
	}
}
