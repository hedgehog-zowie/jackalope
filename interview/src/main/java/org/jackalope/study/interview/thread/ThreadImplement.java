package org.jackalope.study.interview.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class ThreadImplement {

    public static void main(String args[]) throws ExecutionException, InterruptedException {
        Thread thread1 = new ThreadImpl1();
        Thread thread2 = new Thread(new ThreadImpl2());
        FutureTask<String> futureTask = new FutureTask<String>(new ThreadImpl3());
        Thread thread3 = new Thread(futureTask);
        thread1.start();
        thread2.start();
        thread3.start();
        System.out.println(futureTask.get());
    }

    static class ThreadImpl1 extends Thread {
        @Override
        public void run() {
            int i = 0;
            while (i++ < 10) {
                System.out.println(this.getClass() + " run " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ThreadImpl2 implements Runnable {
        @Override
        public void run() {
            int i = 0;
            while (i++ < 10) {
                System.out.println(this.getClass() + " run " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ThreadImpl3 implements Callable<String> {
        @Override
        public String call() throws Exception {
            int i = 0;
            while (i++ < 10) {
                System.out.println(this.getClass() + " run " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "ThreadImpl3";
        }
    }

}
