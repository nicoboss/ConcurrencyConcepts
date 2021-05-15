package main

import (
	"fmt"
)

//Basieren auf C++ splitmix PRNG von Arvid Gerstmann.
func random(seed uint64, result chan uint64) {
	for {
		seed += 0x9E3779B97F4A7C15
		var z uint64 = seed
		z = (z ^ (z >> 30)) * 0xBF58476D1CE4E5B9
		z = (z ^ (z >> 27)) * 0x94D049BB133111EB
		fmt.Println("Generated!")
		result <- (z ^ (z >> 31)) >> 31
	}
}

func main() {
	cache := 10
	tasks := 100
	result := make(chan uint64, cache)
	go random(0, result)
	for i := 0; i < tasks; i++ {
		value := <-result
		fmt.Println(value)
	}
	fmt.Println("Done")
}
