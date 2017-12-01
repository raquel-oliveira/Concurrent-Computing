package main

import(
	"fmt"
	"math/rand"
	"time"
	"strconv"
	"sync"
)

const (
	MIN_TIME = 15;
	MAX_TIME = 120;
	NUMBER_COMPUTERS = 8;
	NUMBER_TEENAGERS = 26;
)

func init() {
	rand.Seed(time.Now().Unix())
}

// source: source: http://golangcookbook.blogspot.com.br/2012/11/generate-random-number-in-given-range.html
func random(min, max int) int {
	return rand.Intn(max - min) + min
}

func teenName(i int) string {
	name := "Adolescente ";
	name += string(rune('A' + i));
	name += " . ";
	return name;
}


func waitingRoom(in chan string, out chan string){
	waitingPeople := []string{}


	for {
		select{
			case user :=<- in:
				if len(waitingPeople) > 0 {
					fmt.Println(user + "está aguardando");
					waitingPeople = append(waitingPeople, user);
				} else{
					select {
						case out <- user:
							break;
						default:
							fmt.Println(user + "está aguardando");
							waitingPeople = append(waitingPeople, user);
					}
				}
			default:
				if (len(waitingPeople) > 0){
					out <-waitingPeople[0]
					waitingPeople = waitingPeople[1:]
				}  else{
					//break;
					close(out)
					return;
				}
		}
	}
}

func useComputers(in chan string, wg *sync.WaitGroup){
//	defer wg.Done();
	for {
		user, check := <-in;
		if !check{
			break ;
		}
		fmt.Println(user + "está on-line ");
		interval := random(MIN_TIME, MAX_TIME);
		time.Sleep(time.Duration(interval/5) * time.Second);
		fmt.Println(user + "liberou a máquina após passar "+ strconv.Itoa(interval) +" minutos on-line");
	}
	wg.Done()
}

func main(){
	users := make(chan string, NUMBER_TEENAGERS);
	var wg sync.WaitGroup;
	c := make(chan string);

	for computer := 0; computer < NUMBER_COMPUTERS; computer++ {
		wg.Add(1);
		go useComputers(c, &wg);
	}

	go waitingRoom(users, c);

	/*
	//Simulate clients randomly
	sorted := rand.Perm(NUMBER_TEENAGERS)

	for i:= 0; i < NUMBER_TEENAGERS; i++ {
		users <- teenName(sorted[i]);
	}*/
	for i:= 0; i < NUMBER_TEENAGERS; i++ {
		users <- teenName(i);
	}
	//close(users);
	wg.Wait();
	fmt.Println("A lan-house está finalmente vazia e todos foram atendidos")

}
