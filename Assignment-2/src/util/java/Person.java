import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Person implements Runnable {
    public static final double timeMin = 0.1;
    public static final double timeMax = 5.0;

    private static AtomicInteger atomicId = new AtomicInteger();
    private int id;
    private SEX sex;
    private double timeLimit; // time needed to make necessity(or not) in the bathroom
    private Bathroom instanceBathroom;

    public Person(SEX sex, Bathroom b) {
        this.id = atomicId.incrementAndGet();
        this.sex = sex;
        Random r = new Random();
        this.timeLimit = timeMin + (timeMax - timeMin) * r.nextDouble();
        this.instanceBathroom = b;
    }

    public Integer getId() {
        return id;
    }

    public SEX getSex() {
        return sex;
    }

    public double getTime() {return timeLimit;}

    @Override
    public void run() {
        try {
            sleep((long) timeLimit); //Using bathroom to do something ( ͡° ͜ʖ ͡°)
            if (instanceBathroom instanceof BathroomSemaphore) {
                BathroomSemaphore bathS = (BathroomSemaphore) instanceBathroom;
                bathS.leave(this);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
