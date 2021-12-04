package main

import (
	"fmt"
	"io"
	"sort"
)

const inputJolts = 0

var lookups = make(map[int]int)

func calculate(adapters []int, index int) int {
	n := len(adapters)
	var a int
	count, exists := lookups[index]
	if exists {
		return count
	}
	switch index {
	case n - 1:
		return 1
	case -1:
		a = 0
	default:
		a = adapters[index]
	}
	for i := index + 1; i <= index+3 && i < n; i++ {
		if adapters[i]-a <= 3 {
			count += calculate(adapters, i)
		} else {
			break
		}
	}
	lookups[index] = count
	return count
}

func findDiffCountsProduct(adapters []int) (int, error) {
	sort.Ints(adapters)
	onesCount := 0
	threesCount := 0
	last := inputJolts
	for _, a := range adapters {
		diff := a - last
		switch diff {
		case 1:
			onesCount++
		case 2:
			// ignore
		case 3:
			threesCount++
		default:
			return 0, fmt.Errorf("Invalid jump %d for %d to %d", diff, last, a)
		}
		last = a
	}
	threesCount++
	return onesCount * threesCount, nil
}

func main() {
	var numbers []int
	for {
		var number int
		_, err := fmt.Scanf("%d", &number)
		if err == io.EOF {
			break
		}
		numbers = append(numbers, number)
	}
	sort.Ints(numbers)
	count := calculate(numbers, -1)
	fmt.Println(count)
}
