package main

import (
	"fmt"
	"io"
	"sort"
)

const inputJolts = 0

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
	prod, err := findDiffCountsProduct(numbers)
	if err != nil {
		panic(err)
	}
	fmt.Println(prod)
}
