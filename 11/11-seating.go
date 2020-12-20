package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"strings"
)

type cellT rune

const (
	floor    cellT = '.'
	empty          = 'L'
	occupied       = '#'
)

type seatingT [][]cellT

func (seating seatingT) width() int {
	if len(seating) > 0 {
		return len(seating[0])
	}
	return 0
}

func (seating seatingT) height() int {
	return len(seating)
}

func checkBounds(v int, size int) bool {
	return 0 <= v && v < size
}

func (seating seatingT) nextCell(row int, col int) cellT {
	height := seating.height()
	width := seating.width()
	occ := 0
	for j := row - 1; j <= row+1; j++ {
		if !checkBounds(j, height) {
			continue
		}
		for i := col - 1; i <= col+1; i++ {
			if !checkBounds(i, width) || j == row && i == col {
				continue
			}
			if seating[j][i] == occupied {
				occ++
			}
		}
	}
	switch seating[row][col] {
	case floor:
		return floor
	case empty:
		if occ == 0 {
			return occupied
		}
		return empty
	case occupied:
		if occ >= 4 {
			return empty
		}
		return occupied
	default:
		panic(fmt.Errorf("illegal seat state %c", seating[row][col]))
	}
}

func (seating seatingT) nextSeating() seatingT {
	height := seating.height()
	width := seating.width()
	next := make(seatingT, 0, height)
	for j := 0; j < height; j++ {
		row := make([]cellT, 0, width)
		for i := 0; i < width; i++ {
			row = append(row, seating.nextCell(j, i))
		}
		next = append(next, row)
	}
	fmt.Println("next")
	fmt.Print(next)
	return next
}

func (seating seatingT) apply(f func(cellT, int, int) bool) {
	height := seating.height()
	width := seating.width()
	for j := 0; j < height; j++ {
		for i := 0; i < width; i++ {
			if !f(seating[j][i], j, i) {
				return
			}
		}
	}
}

func (seating seatingT) countOccupied() int {
	count := 0
	seating.apply(func(c cellT, j int, i int) bool {
		if c == occupied {
			count++
		}
		return true
	})
	return count
}

func (seating seatingT) equals(other seatingT) bool {
	equal := true
	seating.apply(func(c cellT, j int, i int) bool {
		if other[j][i] != c {
			equal = false
			return false
		}
		return true
	})
	return equal
}

func (seating seatingT) String() string {
	row := 0
	var b strings.Builder
	seating.apply(func(c cellT, j int, i int) bool {
		if j != row {
			b.WriteRune('\n')
			row = j
		}
		b.WriteRune(rune(c))
		return true
	})
	b.WriteRune('\n')
	return b.String()
}

func (seating seatingT) stabilize() seatingT {
	current := seating
	var next seatingT
	for {
		next = current.nextSeating()
		if next.equals(current) {
			break
		}
		current = next
	}
	return current
}

func main() {
	var seating seatingT
	reader := bufio.NewReader(os.Stdin)
	for row := []cellT{}; true; {
		c, _, err := reader.ReadRune()
		if err == io.EOF {
			break
		}
		if err != nil {
			panic(err)
		}
		switch c {
		case '\n':
			seating = append(seating, row)
			row = []cellT{}
		default:
			row = append(row, cellT(c))
		}
	}
	fmt.Print(seating)
	fmt.Println(seating.stabilize().countOccupied())
}
