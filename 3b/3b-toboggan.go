package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
)

type Slope struct {
	right int
	down  int
}

func evalAngle(treeMap [][]bool, slope Slope) int {
	var count, i, j int
	width := len(treeMap[0])
	for {
		i = (i + slope.right) % width
		j += slope.down
		if j >= len(treeMap) {
			break
		}
		if treeMap[j][i] {
			count++
		}
	}
	return count
}

func main() {
	var treeMap [][]bool

	reader := bufio.NewReader(os.Stdin)
	for row := []bool{}; true; {
		rune, _, err := reader.ReadRune()
		if err == io.EOF {
			break
		}
		if err != nil {
			panic(err)
		}
		switch rune {
		case '.':
			row = append(row, false)
		case '#':
			row = append(row, true)
		case '\n':
			treeMap = append(treeMap, row)
			row = []bool{}
		default:
			panic(fmt.Sprintf("Unexpected char %v.", rune))
		}
	}
	product := 1
	for _, slope := range []Slope{{1, 1}, Slope{3, 1}, Slope{5, 1}, Slope{7, 1}, Slope{1, 2}} {
		product *= evalAngle(treeMap, slope)
	}
	fmt.Println(product)
}
