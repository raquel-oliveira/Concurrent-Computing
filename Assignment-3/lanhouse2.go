package main
/*
*
* Program that simulates the use of a lan house in a small town with 8(NUMBER_COMPUTERS)
* In this program it is simulate that 26(NUMBER_TEENAGERS) teenagers have to use the lan house in the day.
* On average, each of these adolescents would spend 15 minutes and 2 hours online.
* When all computers are in use, the next person in the queue has to wait until a computer is released.
*
* This program is follows the specification in 'documents/Prtica-Programao_concorrente_em_Go.pdf'.
*
* @author Raquel Oliveira
* @version 1.4
*/
import(
	"fmt"
	"math/rand"
	"time"
	"strconv"
)

const (
	MIN_TIME = 15;
	MAX_TIME = 120;
	NUMBER_COMPUTERS = 8;
	NUMBER_TEENAGERS = 26;
)

func init() {
	//rand.New(rand.NewSource(time.Now().Unix()))
	rand.Seed(time.Now().Unix()) //Seed for random generations
}

// source: http://golangcookbook.blogspot.com.br/2012/11/generate-random-number-in-given-range.html
func random(min, max int) int {
	return rand.Intn(max - min) + min
}

func teenName(i int) string {
	name := "Adolescente ";
	name += string(rune('A' + i));
	name += " . ";
	return name;
}

func useComputer(user string, pc int, finishUseComputer chan int) {
	fmt.Println(user + "está on-line ");
	interval := random(MIN_TIME, MAX_TIME);
	time.Sleep(time.Duration(interval/5) * time.Second);
	fmt.Println(user + "liberou a máquina após passar "+ strconv.Itoa(interval) +" minutos on-line");
	finishUseComputer <- pc
}


func lanHouse(users chan string, done chan int){
	computerAvailable := make(chan int)     // someone needs to use computer channel
	teenagersList := []string{}
	allPeopleIn := false
	freeComputers := []int{}

	// Turning on the computers. obs: didn't need to be a list
	for i := 1; i <= NUMBER_COMPUTERS; i++ {
		freeComputers = append(freeComputers, i)
	}

	for {
		select {
		case computer := <-computerAvailable: // computer is free (finishUseComputer)
			if len(teenagersList) > 0 {  // there is at least someone waiting to use a computer
				client := teenagersList[0]
				teenagersList = teenagersList[1:]
				go useComputer(client, computer, computerAvailable)
			} else {
				// ~waiting to close lanhouse. computer by computer. No more people waiting to use computer
				if allPeopleIn && len(freeComputers) == NUMBER_COMPUTERS -1{
					fmt.Println("A lan-house está finalmente vazia e todos foram atendidos")
					return
				}
				//fmt.Println("Turn off computer %d", computer)
				freeComputers = append(freeComputers, computer)
			}

		case client := <-users:
			// Use compute
			if len(freeComputers) > 0  {
				computer := freeComputers[0]
				freeComputers = freeComputers[1:]
				go useComputer(client, computer, computerAvailable)
			} else {
				teenagersList = append(teenagersList, client)
				fmt.Println(client + "está aguardando");
			}

		case <-done:
			//fmt.Println("No more students allowed here to (wait to) use lan house today")
			allPeopleIn = true
		}
	}
}

func main(){
	clients := make(chan string)         // A channel of users
	done := make(chan int)              // Close the lanHouse

	// Simulate clients
	go func () {

		sorted := rand.Perm(NUMBER_TEENAGERS) //sorted list. ("Perm returns, as a slice of n ints, a pseudo-random permutation of the integers [0,n) from the default Source.")

		for i:= 0; i < NUMBER_TEENAGERS; i++ {
			time.Sleep(time.Duration(rand.Int() % 20) * time.Millisecond)
			clients <- teenName(sorted[i]);
		}
		done <- 1
	}()

	// Simulate Lan House
	lanHouse(clients, done)
}
