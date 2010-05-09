package ox.seva.util;

public class Semaphore {
    private boolean taken = false;
    public boolean isTaken() {
        return taken;
    }
    public synchronized void takeSemaphore() throws InterruptedException {
        taken = true;
        wait();
    }
    public synchronized void  giveSemaphore(){
        taken = false;
        notifyAll();
    }
}