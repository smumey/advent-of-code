package main

import (
	"fmt"
	"io"
	"sort"
)

const preambleLength = 25

// func push(numbers int[], num int)
// {
// 	n := len(numbers)
// 	for i := 1; i < len(numbers); i++ {
// 		numbers[i-1] = numbers[i]
// 	}
// 	numbers[n]
// }

func findMismatch(numbers []int, targetSum int) bool {
	n := len(numbers)
	if n < preambleLength {
		return false
	}
	terms := make([]int, preambleLength)
	copy(terms, numbers[n-preambleLength:])
	sort.Ints(terms)
	for i, t1 := range terms {
		for _, t2 := range terms[i+1:] {
			diff := targetSum - (t1 + t2)
			if diff == 0 {
				return false
			} else if diff < 0 {
				break
			}
		}
	}
	return true
}

func main() {
	numbers := make([]int, 0, preambleLength)
	for {
		var number int
		_, err := fmt.Scanf("%d", &number)
		if err == io.EOF {
			break
		}
		if findMismatch(numbers, number) {
			fmt.Println(number)
			break
		}
		numbers = append(numbers, number)
	}

}
