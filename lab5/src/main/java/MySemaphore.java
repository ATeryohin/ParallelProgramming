import java.beans.IntrospectionException;
import java.net.CacheResponse;
import java.util.concurrent.Semaphore;
public class MySemaphore extends Semaphore {
    private int permits;

    public MySemaphore(int permits) {
        super(permits);
        this.permits = permits;
    }

    public MySemaphore(int permits, boolean fair) {
        super(permits, fair);
    }

    @Override
    public synchronized void acquire() {
        if(permits > 0){
            permits--;
        }else{
            try {
                this.wait();
                permits--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void release(){
        permits++;
        if(permits > 0)
            this.notify();
    }


}
