
//Solution of Shared Bathroom problem
public class MainSemaphore {
    public static void main(String args[]) throws InterruptedException {
        int capacity = 5;

        BathroomSemaphore bathroom = new BathroomSemaphore(capacity);
        Thread b = new Thread(bathroom);
        b.start();
        try {
            b.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
