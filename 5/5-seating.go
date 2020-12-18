package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
)

type docR = map[string]string

func main() {
	var passes = []uint16{}

	reader := bufio.NewReader(os.Stdin)
	for pass := uint16(0); ; {
		c, _, err := reader.ReadRune()
		if err == io.EOF {
			break
		}
		if err != nil {
			panic(err)
		}
		switch c {
		case 'F', 'L':
			pass <<= 1
		case 'B', 'R':
			pass <<= 1
			pass++
		case '\n':
			passes = append(passes, pass)
			pass = 0
		}
	}
	fmt.Println(passes)
	highest := uint16(0)
	for _, pass := range passes {
		if pass > highest {
			highest = pass
		}
	}
	fmt.Println(highest)
}
