package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

type rangeT struct {
	low  int
	high int
}

type fieldDefT struct {
	name   string
	ranges []rangeT
}

func (fieldDef fieldDefT) isValid(value int) bool {
	for _, r := range fieldDef.ranges {
		if r.low <= value && value <= r.high {
			return true
		}
	}
	return false
}

func toRanges(sRanges string) []rangeT {
	nRanges := []rangeT{}
	for _, sRange := range strings.Split(sRanges, " or ") {
		nRange := rangeT{}
		fmt.Sscanf(sRange, "%d-%d", &nRange.low, &nRange.high)
		nRanges = append(nRanges, nRange)
	}
	return nRanges
}

func toValues(csvValues string) []int {
	values := []int{}
	for _, sVal := range strings.Split(csvValues, ",") {
		v, _ := strconv.Atoi(sVal)
		values = append(values, v)
	}
	return values
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	var fieldDefs []fieldDefT
	var myTicket []int
	var otherTickets [][]int

	for scanner.Scan() {
		line := scanner.Text()
		if line == "" {
			break
		}
		parts := strings.SplitN(line, ": ", 2)
		fieldDefs = append(fieldDefs, fieldDefT{parts[0], toRanges(parts[1])})
	}

	{
		scanner.Scan()
		if scanner.Text() != "your ticket:" {
			panic("failed reading your ticket")
		}
		scanner.Scan()
		myTicket = toValues(scanner.Text())
		scanner.Scan()
		scanner.Scan()
		if scanner.Text() != "nearby tickets:" {
			panic("failed reading nearby tickets")
		}
	}

	for scanner.Scan() {
		otherTickets = append(otherTickets, toValues(scanner.Text()))
	}

	fmt.Printf("defs: %v\nmy: %v\nothers: %v\n", fieldDefs, myTicket, otherTickets)

	errorRate := 0
	for _, ticket := range otherTickets {
		for _, v := range ticket {
			match := false
			for _, def := range fieldDefs {
				if def.isValid(v) {
					match = true
				}
			}
			if !match {
				errorRate += v
			}
		}
	}
	fmt.Println(errorRate)
}
