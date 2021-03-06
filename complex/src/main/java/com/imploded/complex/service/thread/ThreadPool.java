package com.imploded.complex.service.thread;

import java.util.concurrent.*;

/**
 * 线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
 * 说明：Executors返回的线程池对象的弊端如下：
 * 1）FixedThreadPool和SingleThreadPool:
 *   允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
 * 2）CachedThreadPool:
 *   允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。
 *
 * @author shuai.yang
 */
public class ThreadPool {

    public static void main(String[] args) {
        createCustomThreadPool();
    }

    /**
     * Executors.newCachedThreadPool()
     * 若线程不够则创建, 若线程数超出所需,则缓存一段时间后回收
     */
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

    /**
     * 创建一个周期性的线程池, 支持定时及周期性的执行
     */
    public static void createScheduledThreadPool() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executorService.schedule(() -> {
                        System.out.println(Thread.currentThread().getName());

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 延迟3秒执行
                    , 3, TimeUnit.SECONDS);
        }
    }

    /**
     * 创建一个单线程的线程池, 只有1个线程顺序执行
     */
    public static void createSingleThreadPool() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName());
            });
        }
    }

    /**
     * 自定义线程池
     * corePoolSize: 核心线程数
     * maxximumPoolSize: 最大线程数
     * keepAliveTime: 存活时间
     */
    public static void createCustomThreadPool() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,
                10,
                10,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(5, true),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy()
        );
        for (int i = 0; i < 30; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
