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

func matches(ticket []int, fieldDefs []fieldDefT) bool {
	for _, v := range ticket {
		match := false
		for _, def := range fieldDefs {
			if def.isValid(v) {
				match = true
			}
		}
		if !match {
			return false
		}
	}
	return true
}

func calcMatches(fieldDefs []fieldDefT, tickets [][]int) [][]fieldDefT {
	maxFieldNum := len(fieldDefs)
	matches := make([][]fieldDefT, maxFieldNum)

	for fieldNum := 0; fieldNum < maxFieldNum; fieldNum++ {
		fieldMatches := []fieldDefT{}
		match := false
		for _, fieldDef := range fieldDefs {
			match = true
			for _, ticket := range tickets {
				v := ticket[fieldNum]
				if !fieldDef.isValid(v) {
					match = false
					break
				}
			}
			if match {
				// fmt.Printf("field def %v matches field num %d\n", fieldDef, fieldNum)
				fieldMatches = append(fieldMatches, fieldDef)
			}
		}
		fmt.Printf("fieldNum %d has %d matches: %v\n", fieldNum, len(fieldMatches), fieldMatches)
		matches[fieldNum] = fieldMatches
	}
	return matches
}

func remove(defs []fieldDefT, rmDef fieldDefT) (bool, []fieldDefT) {
	for i, def := range defs {
		if def.name == rmDef.name {
			defs[i] = defs[len(defs)-1]
			return true, defs[0 : len(defs)-1]
		}
	}
	return false, defs
}

func assign(matches [][]fieldDefT) [][]fieldDefT {
	changed := false
	for i, fieldMatches := range matches {
		if len(fieldMatches) == 1 {
			for j, m := range matches {
				if i != j {
					var c bool
					c, matches[j] = remove(m, fieldMatches[0])
					changed = changed || c
				}
			}
		}
	}
	if changed {
		return assign(matches)
	}
	return matches
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
		t := toValues(scanner.Text())
		if matches(t, fieldDefs) {
			otherTickets = append(otherTickets, toValues(scanner.Text()))
		}
	}

	for _, t := range otherTickets {
		if len(t) != len(fieldDefs) {
			panic(fmt.Errorf("ticket %v has length %d different from defs %d", t, len(t), len(fieldDefs)))
		}
	}

	matches := calcMatches(fieldDefs, append(otherTickets, myTicket))
	assign(matches)

	for i, m := range matches {
		fmt.Printf("%d: %d %v\n\n", i, len(m), m)
	}

	product := 1
	for i, m := range matches {
		if strings.Index(m[0].name, "departure") == 0 {
			product *= myTicket[i]
		}
	}
	fmt.Println(product)
}
