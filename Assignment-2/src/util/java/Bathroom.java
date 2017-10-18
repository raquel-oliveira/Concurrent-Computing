import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public abstract class Bathroom {
    static final int multiply = 3;

    protected int capacity;
    protected Queue waiting_list;
    protected List<Person> occupation_list;
    //Number of inside people of each sex:
    protected int num_men;
    protected int num_women;

    public Bathroom(int capacity){
        this.capacity = capacity;
        this.num_men = 0;
        this.num_women = 0;
        this.occupation_list = new ArrayList<Person>();
        this.waiting_list = new ConcurrentLinkedQueue<Person>();
    }


    public Boolean isEmpty(){
        if ((num_men == 0) && (num_women == 0)) {
            return true;
        } else {
            return false;
        }
    }

    /*
    @return number of people inside the bathroom
    */
    public int getNumberOfPeople(){
        //TODO: check inconsistency: (occupation_list.size() != (num_men+num_women))
        return occupation_list.size();
    }

    public void addPerson(Person p){
        occupation_list.add(p);
        if (p.getSex()== SEX.Women){
            num_women++;
        } else {
            num_men++;
        }
    }

    public void addMen(Person p){
        occupation_list.add(p);
        num_men++;
    }

    public void addWomen(Person p){
        occupation_list.add(p);
        num_women++;
    }

    /** Populate waiting list to use bathroom
     * Create @multiply times more people than allowed to use the toilet.
     * **/
    public void populate() {
        for (int i = 0; i < capacity*multiply; i++){
            int aux = (int) Math.round(Math.random());
            if (aux == 0){
                waiting_list.add(new Person(SEX.Men, this));
            } else{
                waiting_list.add(new Person(SEX.Women, this));
            }
        }
    }
}