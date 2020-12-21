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
	empty    cellT = 'L'
	occupied cellT = '#'
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
	occ := seating.observeCount(row, col)
	switch seating[row][col] {
	case floor:
		return floor
	case empty:
		if occ == 0 {
			return occupied
		}
		return empty
	case occupied:
		if occ >= 5 {
			return empty
		}
		return occupied
	default:
		panic(fmt.Errorf("illegal seat state %c", seating[row][col]))
	}
}

func nextPosGen(rowInc int, colInc int) func(int, int) (int, int) {
	return func(row int, col int) (int, int) {
		return row + rowInc, col + colInc
	}
}

func (seating seatingT) observeCount(row int, col int) int {
	height := seating.height()
	width := seating.width()
	inc := []int{-1, 0, 1}
	occ := 0
	for _, rowInc := range inc {
		for _, colInc := range inc {
			if rowInc == 0 && colInc == 0 {
				continue
			}
			nextPos := nextPosGen(rowInc, colInc)
			var i, j int
			for j, i = nextPos(row, col); checkBounds(j, height) && checkBounds(i, width); j, i = nextPos(j, i) {
				if seating[j][i] == occupied {
					occ++
					break
				} else if seating[j][i] == empty {
					break
				}
			}
		}
	}
	return occ
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
