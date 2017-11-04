import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public abstract class Bathroom {
    private static final Logger LOGGER = Logger.getLogger(Bathroom.class.getName());
    static final int multiply = 3;

    protected volatile int capacity;
    protected volatile Queue waiting_list;
    protected volatile List<Person> occupation_list;
    //Number of inside people of each sex:
    protected volatile int num_men;
    protected volatile int num_women;

    public Bathroom(int capacity){
        LOGGER.info("Logger Name: "+LOGGER.getName());
        this.capacity = capacity;
        this.num_men = 0;
        this.num_women = 0;
        this.occupation_list = new ArrayList<Person>();
        this.waiting_list = new ConcurrentLinkedQueue<Person>();
        LOGGER.info("Capacity of bathroom: "+ capacity+ "\n");
    }


    /**
     * @return true if no one is using bathroom
     * (occupation_list.size() == 0)
     */
    public Boolean isEmpty(){
        if ((num_men == 0) && (num_women == 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
    @return number of people inside the bathroom
    */
    public int getNumberOfPeople(){
        //TODO: check possibility of inconsistency: (occupation_list.size() != (num_men+num_women))
        return occupation_list.size();
    }

    /**
     * Add person inside the bathroom
     * @p person who will be inside the bathroom
    */
    public void addPerson(Person p){
        occupation_list.add(p);
        if (p.getSex()== SEX.Women){
            num_women++;
        } else {
            num_men++;
        }
        LOGGER.info("Person " + p.getId() + "("+ p.getSex() + ") is in the bathroom and will spend " + p.getTime() +"\n");
    }

    public abstract void leave(Person p);

    /** Populate waiting list to use bathroom
     * Create 5(@multiply) times more people than allowed to use the toilet.
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