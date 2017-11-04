import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class BathroomMonitor extends Bathroom implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(BathroomSemaphore.class.getName());

    public BathroomMonitor(int c){
        super(c);
        LOGGER.info("Logger Name: "+LOGGER.getName());
    }

    public synchronized boolean receive(Person p) throws InterruptedException {
        Thread personThread = new Thread(p);
        if (  (p.getSex() == SEX.Men && (occupation_list.isEmpty() || num_men > 0 && num_men < capacity)) || //Can be used by a men
                (p.getSex() == SEX.Women && (occupation_list.isEmpty() || num_women > 0 && num_women < capacity)) //Can be used by a women
            ){
            addPerson(p);
            personThread.start();
            return true;
        }
        return false;
    }

    @Override
    public synchronized void leave(Person p) {
        occupation_list.remove(p);
        if (p.getSex() == SEX.Men) { num_men--;} else { num_women--;}
        LOGGER.info("Person " + p.getId() + "("+ p.getSex() + ") get out of bathroom\n");
    }

    @Override
    public void run() {
        populate();
        while(!waiting_list.isEmpty()){
            try {
                if (receive(waiting_list.peek())) {
                    waiting_list.poll();
                    LOGGER.info("------------------------------\nNumero de homens =" + num_men + "\nNumero de mulheres =" + num_women +"\nQuantas pessoas no banheiro =" + occupation_list.size() + "\nQuantas esperando: " + waiting_list.size() + "\n------------------------------");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
