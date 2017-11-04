
public class MainMonitor {
    static final int capacity = 5;

    public static void main(String args[]){
        BathroomMonitor bathroom = new BathroomMonitor(capacity);
        Thread b = new Thread(bathroom);
        b.start();
        try {
            b.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
