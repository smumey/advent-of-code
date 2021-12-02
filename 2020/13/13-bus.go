package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

typeq busT struct {
	number int
	delay  int
}

func findBusWait(arrival int, buses []busT) int {
	bestWait := -1
	bestBus := 0
	for _, b := range buses {
		w := b.number - (arrival % b.number)
		if w < bestWait || bestWait == -1 {
			bestWait = w
			bestBus = b.number
		}
	}
	return bestBus * bestWait
}

func findConvergentTime(buses []busT) int {
	fmt.Println(buses)
	t := 1
	inc := 1
	for _, b := range buses {
		for s := t; ; s += inc {
			if (s+b.delay)%b.number == 0 {
				t = s
				inc *= b.number
				fmt.Printf("bus %v t %d\n", b, t)
				break
			}
		}
	}
	return t
}

func toBuses(raw []string) []busT {
	var buses []busT
	for i, r := range raw {
		b, err := strconv.Atoi(r)
		if err == nil {
			buses = append(buses, busT{b, i})
		}

	}
	return buses
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	// arrival, _ := strconv.Atoi(scanner.Text())
	scanner.Scan()
	buses := toBuses(strings.Split(scanner.Text(), ","))
	fmt.Printf("buses: %v\n", buses)
	fmt.Println(findConvergentTime(buses))
}
