import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.*;

public class Application {
    private final static int THREAD_POOL_SIZE = 2;

    private static Map<String, Integer> hashmap = null;
    private static Map<String, Integer> hashtable = null;
    private static Map<String, Integer> synchronizedMap = null;
    private static Map<String, Integer> concurrentHashMap = null;


    public static void main(String[] args) throws InterruptedException {

        hashmap = new HashMap<String, Integer>();
        crunchifyPerformTest(hashmap);

        hashtable = new Hashtable<String, Integer>();
        crunchifyPerformTest(hashtable);

        synchronizedMap = Collections.synchronizedMap(new HashMap<String, Integer>());
        crunchifyPerformTest(synchronizedMap);

        concurrentHashMap = new ConcurrentHashMap<String, Integer>();
        crunchifyPerformTest(concurrentHashMap);
        //System.out.println(ForkJoinPool.getCommonPoolParallelism());
    }

    private static void crunchifyPerformTest(final Map<String, Integer> crunchifyThreads) throws InterruptedException {

        System.out.println("Test started for: " + crunchifyThreads.getClass());
        long averageTime = 0;
        for (int i = 0; i < 5; i++) {

            long startTime = System.nanoTime();
            ExecutorService crunchifyExServer = Executors.newFixedThreadPool(THREAD_POOL_SIZE);


            for (int j = 0; j < THREAD_POOL_SIZE; j++) {
                crunchifyExServer.execute (() -> {
                    for (int i1 = 0; i1 < 500000; i1++) {
                        Integer crunchifyRandomNumber = (int) Math.ceil(Math.random() * 550000);

                        // Retrieve value. We are not using it anywhere
                        Integer crunchifyValue = crunchifyThreads.get(String.valueOf(crunchifyRandomNumber));

                        // Put value
                        crunchifyThreads.put(String.valueOf(crunchifyRandomNumber), crunchifyRandomNumber);
                    }
                });
            }
            crunchifyExServer.shutdown();

            // Blocks until all tasks have completed execution after a shutdown request, or the timeout occurs, or the current thread is
            // interrupted, whichever happens first.
            crunchifyExServer.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

            long entTime = System.nanoTime();
            long totalTime = (entTime - startTime) / 1000000L;
            averageTime += totalTime;
            System.out.println(totalTime + " ms");
        }
        System.out.println("For " + crunchifyThreads.getClass() + " the average time is " + averageTime / 5 + " ms\n");
    }
}
