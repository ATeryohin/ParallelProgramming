import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        Semaphore sem = new MySemaphore(1);
        CommonResource res = new CommonResource();
        int pool = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(pool);

        for (int i = 1; i < pool; i++) {
            executorService.execute(new Counter(res, sem, "CountThread-" + i));
        }

        executorService.shutdown();
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);

    }
}
