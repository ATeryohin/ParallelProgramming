package main.java;

import main.java.MySemaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        MySemaphore semaphore = new MySemaphore(2);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i <= 15; i++){
            int finalI = i;
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.lock();
                        Thread.sleep((1000));
                        System.out.println(finalI);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.unlock();
                    }
                }
            }));
        }
        for (Thread t: threads)
            t.start();
    }
}
