package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

type containT = struct {
	container string
	containee string
	count     int
}

var containerRe = regexp.MustCompile(`^.*? bag`)
var containeeRe = regexp.MustCompile(`\d+ .*? bag`)

func findContainers(bag string, contains []containT) map[string]bool {
	containers := make(map[string]bool)
	for _, contain := range contains {
		if contain.containee == bag {
			containers[contain.container] = true
			for bag := range findContainers(contain.container, contains) {
				containers[bag] = true
			}
		}
	}
	return containers
}

func main() {
	var contains []containT

	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		line := scanner.Text()
		fmt.Println(line)
		container := containerRe.FindString(line)
		containees := containeeRe.FindAllString(line, -1)
		fmt.Println(container, ": ", containees)
		for _, countBag := range containees {
			parts := strings.SplitN(countBag, " ", 2)
			count, _ := strconv.Atoi(parts[0])
			contains = append(contains, containT{container, parts[1], count})
		}
	}
	fmt.Println(contains)
	containerMap := findContainers("shiny gold bag", contains)
	fmt.Println(len(containerMap))
}
