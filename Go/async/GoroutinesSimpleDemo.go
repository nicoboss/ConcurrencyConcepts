package main

import (
	"fmt"
	"io/ioutil"
	"net/http"
)

func main() {
	result := make(chan string, 1)
	url := "http://www.nicobosshard.ch/Hi.html"
	go func(url string) {
		response, _ := http.Get(url)
		bodyBytes, _ := ioutil.ReadAll(response.Body)
		result <- string(bodyBytes)
		response.Body.Close()
	}(url)
	value := <-result //Wartet auf Goroutine
	fmt.Println(value)
	close(result)
	fmt.Println("Done")
}
