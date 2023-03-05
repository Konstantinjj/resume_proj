package com.urise.webapp;

import com.urise.webapp.util.LazySingleton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class MainConcurrency {
    private static int counter;
    private static final AtomicInteger atomicCounter = new AtomicInteger();
    //    private static final Object LOCK = new Object();
    private static final Lock lock = new ReentrantLock();
    private static final int THREADS_NUMBER = 10000;

    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        }
    };

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());

        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ", " + getState());
            }
        };
        thread0.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState());
            }
        }).start();

        System.out.println(thread0.getState());

        final MainConcurrency mainConcurrency = new MainConcurrency();
        CountDownLatch latch = new CountDownLatch(THREADS_NUMBER);
        ExecutorService executorService = Executors.newCachedThreadPool();
//        CompletionService completionService = new ExecutorCompletionService(executorService);


//        List<Thread> threads = new ArrayList<>(10000);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            Future<Integer> future = executorService.submit(() -> {

//            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
                }
                latch.countDown();
                return 5;
            });

//            thread.start();
//            threads.add(thread);
        }
//        threads.forEach(t -> {
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
//        System.out.println(counter);
        System.out.println(mainConcurrency.atomicCounter.get());

//        LazySingleton.getInstance();
    }

    private static void inc() {
        atomicCounter.incrementAndGet();
//        lock.lock();
//        try {
//            counter++;
//        } finally {
//            lock.unlock();
//        }
    }

//    либо просто private static synchronized, если нет участков, которые надо обрабатывать в очереди
//    private static void inc() {
//        double a = Math.sin(13.);
//        synchronized (LOCK) {
//            counter++;
//        }
//    }

}
