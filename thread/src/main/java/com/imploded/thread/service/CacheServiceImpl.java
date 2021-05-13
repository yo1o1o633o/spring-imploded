package com.imploded.thread.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 *  线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
 *  说明：Executors返回的线程池对象的弊端如下：
 *  1）FixedThreadPool和SingleThreadPool:
 *        允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
 *  2）CachedThreadPool:
 *        允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。
 *
 * @author shuai.yang*/
public class CacheServiceImpl {

    public static void main(String[] args) {
        createFixedThreadPool();
    }
        /**
     * Executors.newCachedThreadPool()
     * 若线程不够则创建, 若线程数超出所需,则缓存一段时间后回收
     * */
    public static void createCachedThreadPool() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }
    }

    /**
     * Executors.newFixedThreadPool(3)
     * 固定线程数的线程池, 超出的线程会在队列中等待
     */
    private static void createFixedThreadPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName());

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void c() {


        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

        ExecutorService pool = new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), null, new ThreadPoolExecutor.AbortPolicy());
    }
}
