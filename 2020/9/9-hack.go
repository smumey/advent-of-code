package main

import (
	"errors"
	"fmt"
	"io"
	"sort"
)

const preambleLength = 25

var errNotFound = errors.New("not found")

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

func findContiguous(numbers []int, prime int) (int, error) {
	for i, t1 := range numbers {
		s := t1
		min := t1
		max := t1
		for _, t2 := range numbers[i+1:] {
			s += t2
			if t2 < min {
				min = t2
			} else if t2 > max {
				max = t2
			}
			if s == prime {
				return min + max, nil
			}
		}
	}
	return 0, errNotFound
}

func main() {
	numbers := make([]int, 0, preambleLength)
	var prime int
	for {
		var number int
		_, err := fmt.Scanf("%d", &number)
		if err == io.EOF {
			panic("No number found!")
		}
		if findMismatch(numbers, number) {
			prime = number
			break
		}
		numbers = append(numbers, number)
	}
	sum, err := findContiguous(numbers, prime)
	if err != nil {
		panic(err)
	}
	fmt.Println(sum)
}
