package main

import (
	"fmt"
	"io"
	"strconv"
	"strings"
)

const wordLength = uint8(36)

type maskT struct {
	or, wild uint64
}

func readMask(text string) maskT {
	mask := maskT{}
	reader := strings.NewReader(text)
	for i := int(wordLength - 1); i >= 0; i-- {
		b, _, err := reader.ReadRune()
		if err != nil {
			panic(err)
		}
		switch b {
		case 'X':
			mask.wild |= 1
		case '1':
			mask.or |= 1
		case '0':
			// do nothing
		default:
			panic(fmt.Errorf("bad mask char %c", b))
		}
		if i > 0 {
			mask.wild <<= 1
			mask.or <<= 1
		}
	}
	return mask
}

func (mask maskT) addressGen(address uint64) func() uint64 {
	wildBits := []uint8{}
	maskAddr := (address | mask.or) & ^mask.wild
	for i := uint8(0); i < wordLength; i++ {
		if mask.wild&(1<<i) > 0 {
			wildBits = append(wildBits, i)
		}
	}
	wildCount := len(wildBits)
	permuteLimit := 1 << wildCount
	fmt.Printf("maskAddr %036b\n", maskAddr)
	fmt.Printf("wild bits %v permuteLimit %d\n", wildBits, permuteLimit)
	permute := 0
	return func() uint64 {
		if permute == permuteLimit {
			return ^uint64(0)
		}
		newAddr := maskAddr
		i := 0
		for p := permute; p > 0; p >>= 1 {
			if 1&p > 0 {
				newAddr = newAddr | (1 << wildBits[i])
			}
			i++
		}
		// fmt.Printf("%d perm = %036b\n", permute, newAddr)
		permute++
		return newAddr
	}
}

func (mask maskT) String() string {
	return fmt.Sprintf("{or: %036b wild: %036b}", mask.or, mask.wild)
}

func main() {
	mem := make(map[uint64]uint64)
	mask := maskT{0, 1<<wordLength - 1}

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
			raw, _ := strconv.ParseUint(assignment, 10, int(wordLength))
			nextAddr := mask.addressGen(address)
			for addr := nextAddr(); addr != ^uint64(0); addr = nextAddr() {
				mem[addr] = raw
			}
		}
	}
	sum := uint64(0)
	for _, v := range mem {
		sum += v
	}
	fmt.Println(sum)
}
