package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
)

func main() {
	result := make(chan string, 1)
	url := "http://www.nicobosshard.ch/Hi.html"
	go func(url string) {
		response, err := http.Get(url)
		if err != nil {
			log.Fatal("HTTP get error: ", err)
		} else {
			defer response.Body.Close()
			bodyBytes, err := ioutil.ReadAll(response.Body)
			if err != nil {
				log.Fatal("HTTP ReadAll error: ", err)
			} else {
				result <- string(bodyBytes)
			}
		}
	}(url)
	value := <-result //Wartet auf Goroutine
	fmt.Println(value)
	close(result)
	fmt.Println("Done")
}
