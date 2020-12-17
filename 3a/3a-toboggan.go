package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
)

func evalAngle(treeMap [][]bool, right int) int {
	count := 0
	for i, row := range treeMap[1:] {
		pos := (right * (i + 1)) % len(row)
		if row[pos] {
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
			panic(fmt.Sprintf("Unexpect char %v.", rune))
		}
	}
	fmt.Println(evalAngle(treeMap, 3))
}
