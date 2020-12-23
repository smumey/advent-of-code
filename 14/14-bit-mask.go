package main

import (
	"fmt"
	"io"
	"strconv"
	"strings"
)

type maskT struct {
	or, and uint64
}

func readMask(text string) maskT {
	mask := maskT{}
	reader := strings.NewReader(text)
	for i := 35; i >= 0; i-- {
		b, _, err := reader.ReadRune()
		if err != nil {
			panic(err)
		}
		switch b {
		case 'X':
			mask.and |= 1
		case '1':
			mask.and |= 1
			mask.or |= 1
		case '0':
			// do nothing
		default:
			panic(fmt.Errorf("bad mask char %c", b))
		}
		if i > 0 {
			mask.and <<= 1
			mask.or <<= 1
		}
	}
	return mask
}

func (mask maskT) apply(number uint64) uint64 {
	return (number | mask.or) & mask.and
}

func (mask maskT) String() string {
	return fmt.Sprintf("{or: %036b and: %036b}", mask.or, mask.and)
}

func main() {
	mem := make(map[uint64]uint64)
	mask := maskT{0, 1<<36 - 1}

	for {
		var assignee, assignment string
		var address uint64
		_, err := fmt.Scanf("%s = %s", &assignee, &assignment)
		if err == io.EOF {
			break
		} else if err != nil {
			panic(err)
		}
		fmt.Printf("%s = %s\n", assignee, assignment)
		switch assignee {
		case "mask":
			mask = readMask(assignment)
			fmt.Printf("mask=%v\n", mask)
		default:
			_, err := fmt.Sscanf(assignee, "mem[%d]", &address)
			if err != nil {
				panic(fmt.Errorf("bad assignee '%s'", assignee))
			}
			raw, _ := strconv.ParseUint(assignment, 10, 36)
			mem[address] = mask.apply(raw)
			fmt.Printf("read %d (raw %d) into %d\n", mem[address], raw, address)
		}
	}
	sum := uint64(0)
	for _, v := range mem {
		sum += v
	}
	fmt.Println(sum)
}
