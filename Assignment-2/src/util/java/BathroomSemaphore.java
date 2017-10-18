import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class BathroomSemaphore extends Bathroom implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(BathroomSemaphore.class.getName());
    private Semaphore semaphore;

    public BathroomSemaphore(int c){
        super(c);
        this.semaphore = new Semaphore(c, true); //guarantee first-in first-out
        LOGGER.info("Logger Name: "+LOGGER.getName());
    }

    public boolean receive(Person p) throws InterruptedException {
        Thread personThread = new Thread(p);
        if (  (p.getSex() == SEX.Men && (occupation_list.isEmpty() || num_men > 0)) || //Can be used by a men
              (p.getSex() == SEX.Women && (occupation_list.isEmpty() || num_women > 0)) //Can be used by a women
           ) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            addPerson(p);
            personThread.start();
            return true;
        }
        return false;
    }

    public void leave(Person p){
        semaphore.release();
        occupation_list.remove(p);
        if (p.getSex() == SEX.Men) { num_men--;} else { num_women--;}
        LOGGER.info("Person " + p.getId() + "("+ p.getSex() + ") get out of bathroom\n");
    }

    @Override
    public void run() {
        populate();
        while(!waiting_list.isEmpty()){
            try {
                if (receive((Person) waiting_list.peek())) {
                    waiting_list.poll();
                    LOGGER.info("------------------------------\nNumero de homens =" + num_men + "\nNumero de mulheres =" + num_women +"\nQuantas pessoas no banheiro =" + occupation_list.size() + "\nQuantas esperando: " + waiting_list.size() + "\n------------------------------");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}