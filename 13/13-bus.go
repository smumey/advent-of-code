package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func convertBuses(raw []string) []int {
	var buses []int
	for _, r := range raw {
		b, err := strconv.Atoi(r)
		if err == nil {
			buses = append(buses, b)
		}
	}
	return buses
}

func findBusWait(arrival int, buses []int) int {
	bestWait := -1
	bestBus := 0
	for _, b := range buses {
		w := b - (arrival % b)
		if w < bestWait || bestWait == -1 {
			bestWait = w
			bestBus = b
		}
	}
	return bestBus * bestWait
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	arrival, _ := strconv.Atoi(scanner.Text())
	scanner.Scan()
	buses := convertBuses(strings.Split(scanner.Text(), ","))
	fmt.Printf("time: %d buses: %v\n", arrival, buses)
	fmt.Println(findBusWait(arrival, buses))
}
