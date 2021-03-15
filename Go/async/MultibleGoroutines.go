package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
)

func main() {
	tasks := 10        //10 Downloads
	maxGoroutines := 4 //Maximal 4 gleichzeitige Gorutines
	guard := make(chan struct{}, maxGoroutines)
	result := make(chan string, tasks)
	url := "http://www.nicobosshard.ch/Hi.html"
	for i := 0; i < tasks; i++ {
		guard <- struct{}{}
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
			<-guard
		}(url)
	}
	for i := 0; i < tasks; i++ {
		value := <-result //Wartet auf nächste Goroutine
		fmt.Println(value)
	}
	fmt.Println("Done")
}
