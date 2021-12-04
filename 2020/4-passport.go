package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

type docR = map[string]string

type fieldR = struct {
	name     string
	required bool
	validate func(string) bool
}

func validateYear(yearS string, startYear int, endYear int) bool {
	year, err := strconv.Atoi(yearS)
	if err != nil {
		return false
	}
	return startYear <= year && year <= endYear
}

var fields = []fieldR{
	fieldR{"byr", true, func(s string) bool { return validateYear(s, 1920, 2002) }},
	fieldR{"iyr", true, func(s string) bool { return validateYear(s, 2010, 2020) }},
	fieldR{"eyr", true, func(s string) bool { return validateYear(s, 2020, 2030) }},
	fieldR{"hgt", true, func(s string) bool {
		var scalar float64
		var unit string
		n, _ := fmt.Sscanf(s, "%f%s", &scalar, &unit)
		if n == 0 {
			return false
		}
		switch unit {
		case "cm":
			return 150.0 <= scalar && scalar <= 193.0
		case "in":
			return 59.0 <= scalar && scalar <= 76.0
		default:
			return false
		}
	}},
	fieldR{"hcl", true, func(s string) bool { m, _ := regexp.MatchString(`^#[0-9a-f]{6}$`, s); return m }},
	fieldR{"ecl", true, func(s string) bool { m, _ := regexp.MatchString(`^(amb|blu|brn|gry|grn|hzl|oth)$`, s); return m }},
	fieldR{"pid", true, func(s string) bool { m, _ := regexp.MatchString(`^[0-9]{9}$`, s); return m }},
	fieldR{"cid", false, func(s string) bool { return true }},
}

func isValidDoc(doc docR) bool {
	for _, f := range fields {
		val, present := doc[f.name]
		if f.required && !present {
			return false
		}
		if present && !f.validate(val) {
			fmt.Printf("doc %v failed %v validation\n", doc, f.name)
			return false
		}
	}
	return true
}

func main() {
	document := make(docR)
	documents := []map[string]string{document}

	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		line := scanner.Text()
		if len(line) == 0 {
			document = make(map[string]string)
			documents = append(documents, document)
			continue
		}
		for _, token := range strings.Split(line, " ") {
			parts := strings.Split(token, ":")
			// fmt.Printf("token=%s -- %v\n", token, parts)
			document[parts[0]] = parts[1]
		}
	}
	count := 0
	for _, document := range documents {
		if isValidDoc(document) {
			count++
		}
	}
	fmt.Println(count)
}
