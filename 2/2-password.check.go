package main

import (
	"fmt"
	"io"
	"os"
)

type PasswordCheck struct {
	lower    int
	upper    int
	char     rune
	password string
}

func (check PasswordCheck) isValid() bool {
	count := 0
	for i, c := range check.password {
		if check.char == c && (i == check.lower-1 || i == check.upper-1) {
			count += 1
		}
	}
	return count == 1
}

func main() {
	validCount := 0
	for {
		check := PasswordCheck{}
		// var pass string
		// var lower, upper uint32
		// var char rune
		_, err := fmt.Fscanf(
			os.Stdin,
			"%d-%d %c: %s",
			&check.lower,
			&check.upper,
			&check.char,
			&check.password)
		if err == io.EOF {
			break
		}
		if err != nil {
			panic(err)
		}
		// check.password = pass
		if check.isValid() {
			validCount += 1
		}
	}
	fmt.Println(validCount)
}
