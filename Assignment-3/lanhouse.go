package main

import(
"fmt"
"math/rand"
"time"
)
const minTime = 15;
const maxTime = 120;

/*
 Generates a random number given a range
 @param min minimum number
 @param max maximum number
 @return a random number between min and max

 source: source: http://golangcookbook.blogspot.com.br/2012/11/generate-random-number-in-given-range.html
*/
func random(min, max int) int {
	rand.Seed(time.Now().Unix())
	return rand.Intn(max - min) + min
}

func teenName(i int) string {
	name := "Adolescente ";
	name += string(rune('A' + i));
	name += " . ";
	return name;
}


func useComputer(in <- chan string){
	for {
		user, check := <-in;
		if !check{
			return ;
		}
		fmt.Println(string(user) + "está on-line ");
		interval := random(minTime, maxTime);
		time.Sleep(time.Duration(interval/interval) * time.Second);
		fmt.Println(string(user) + "liberou a máquina após passar ", interval, " minutos on-line");
	}
}

func main(){
	numberComputers := 8;
	numberTeenagers := 26;
	//a := make(chan string);
	b := make(chan string);
	for computer := 0; computer < numberComputers; computer++ {
		go useComputer(b);
	}
	for teen := 0; teen < numberTeenagers ; teen++  {
		b <- (teenName(teen));

	}
}
