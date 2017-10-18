import java.util.concurrent.Semaphore;

public class BathroomSemaphore extends Bathroom implements Runnable{
    private Semaphore semaphore;

    public BathroomSemaphore(int c){
        super(c);
        this.semaphore = new Semaphore(c, true); //guarantee first-in first-out
    }

    public boolean receive(Person p) throws InterruptedException {
        Thread personThread = new Thread(p);
        //Check if bathroom is empty
        if (isEmpty()){
            try {
                semaphore.acquire();
                System.out.println("Entrei "+ p.getId()+"for "+ p.getTime() + p.getSex());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            addPerson(p); //Can receive of any gender
            personThread.start();
            return true;
        } else{
            //busy by men and not full(checked by Semaphore)
            if (p.getSex() == SEX.Men && num_men > 0){
                try {
                    semaphore.acquire();
                    System.out.println("Entrei "+ p.getId()+"for "+ p.getTime() + p.getSex());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                addMen(p);
                personThread.start();
                return true;

            } else
            //busy by women and not full(checked by Semaphore)
            if (p.getSex() == SEX.Women && num_women > 0){
                try {
                    semaphore.acquire();
                    System.out.println("Entrei "+ p.getId()+"for "+ p.getTime() + p.getSex());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                addWomen(p);
                personThread.start();
                return true;
            }
        }
        return false;
    }

    public void leave(Person p){
        semaphore.release();
        occupation_list.remove(p);
        if (p.getSex() == SEX.Men) { num_men--;} else { num_women--;}
    }

    @Override
    public void run() {
        populate();
        while(!waiting_list.isEmpty()){
            try {
                if (receive((Person) waiting_list.peek())) {
                    System.out.println("------------------------------\nNum homens =" + num_men + "\nNum mulheres =" + num_women +"\nQuantas pessoas no banheiro =" + occupation_list.size() + "\nQuantas esperando: " + waiting_list.size());
                    waiting_list.poll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}