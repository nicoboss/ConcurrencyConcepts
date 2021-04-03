//Basierend auf https://hackernoon.com/asyncawait-in-golang-an-introductory-guide-ol1e34sg

package main

import (
	"context"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
)

type Future interface {
	Await() interface{}
}

type future struct {
	await func(ctx context.Context) interface{}
}

func (f future) Await() interface{} {
	return f.await(context.Background())
}

func async(function func() interface{}) Future {
	var result interface{}
	channel := make(chan struct{})
	go func() {
		defer close(channel)
		result = function()
	}()
	return future{
		await: func(context context.Context) interface{} {
			select {
			case <-context.Done():
				return context.Err()
			case <-channel:
				return result
			}
		},
	}
}

func asyncHttpRequest() string {
	url := "http://www.nicobosshard.ch/Hi.html"
	response, err := http.Get(url)
	if err != nil {
		log.Fatal("HTTP get error: ", err)
	}
	defer response.Body.Close()
	bodyBytes, err := ioutil.ReadAll(response.Body)
	if err != nil {
		log.Fatal("HTTP ReadAll error: ", err)
	}
	return string(bodyBytes)
}

func main() {
	future := async(func() interface{} {
		return asyncHttpRequest()
	})
	fmt.Println("Task läuft...")
	val := future.Await()
	fmt.Println(val)
}
