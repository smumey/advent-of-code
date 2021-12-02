package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
)

func countGroup(group uint32) int {
	count := 0
	for i := 0; i < 32; i++ {
		if group&(1<<i) > 0 {
			count++
		}
	}
	fmt.Println(count)
	return count
}

func main() {
	group := ^uint32(0)
	person := uint32(0)
	var groups []uint32
	reader := bufio.NewReader(os.Stdin)
	last := '\n'
	for {
		c, _, err := reader.ReadRune()
		if err == io.EOF {
			break
		}
		if err != nil {
			panic(err)
		}
		switch c {
		case '\n':
			if last == '\n' {
				groups = append(groups, group)
				group = ^uint32(0)
			} else {
				group &= person
				person = 0
			}
		default:
			person |= 1 << (c - 'a')
		}
		last = c
	}
	groups = append(groups, group)
	fmt.Println(groups)
	sum := 0
	for _, group := range groups {
		sum += countGroup(group)
	}
	fmt.Println(sum)
}
