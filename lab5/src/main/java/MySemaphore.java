package main.java;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class MySemaphore {

    private int maxThread, currentThread;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    void lock(){
        try{
            lock.lock();
            while (currentThread >= maxThread){
                condition.await();
            }
            currentThread++;
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    void unlock(){
        lock.lock();
        try {
            currentThread--;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    MySemaphore(int maxThread) {
        this.maxThread = maxThread;
        currentThread = 0;
    }
}