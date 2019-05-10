package main.java;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.*;

public class Application {
    private final static int THREAD_POOL_SIZE = 8;

    private static Map<String, Integer> hashmap = null;
    private static Map<String, Integer> hashtable = null;
    private static Map<String, Integer> synchronizedMap = null;
    private static Map<String, Integer> concurrentHashMap = null;

    public static void main(String[] args) throws InterruptedException {

        hashmap = new HashMap<String, Integer>();
        PerformTest(hashmap);

        hashtable = new Hashtable<String, Integer>();
        PerformTest(hashtable);

        synchronizedMap = Collections.synchronizedMap(new HashMap<String, Integer>());
        PerformTest(synchronizedMap);

        concurrentHashMap = new ConcurrentHashMap<String, Integer>();
        PerformTest(concurrentHashMap);
    }

    private static void PerformTest(final Map<String, Integer> map) throws InterruptedException {

        System.out.println("Test started for: " + map.getClass());
        long averageTime = 0;
        for (int i = 0; i < 5; i++) {

            long startTime = System.nanoTime();
            ExecutorService ExServer = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            for (int j = 0; j < THREAD_POOL_SIZE; j++) {
                ExServer.execute (() -> {
                    for (int i1 = 0; i1 < 500000; i1++) {
                        Integer RandomNumber = (int) Math.ceil(Math.random() * 550000);

                        // Retrieve value. We are not using it anywhere
                        Integer Value = map.get(String.valueOf(RandomNumber));

                        // Put value
                        map.put(String.valueOf(RandomNumber), RandomNumber);
                    }
                });
            }
            ExServer.shutdown();

            ExServer.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

            long entTime = System.nanoTime();
            long totalTime = (entTime - startTime) / 1000000L;
            averageTime += totalTime;
            System.out.println(totalTime + " ms");
        }
        System.out.println("For " + map.getClass() + " the average time is " + averageTime / 5 + " ms\n");
    }
}
