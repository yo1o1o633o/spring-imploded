package com.imploded.async.service.thread;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ThreadLocalServiceImpl implements ThreadLocalService {
    @Autowired
    Executor executor;

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private static InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    private static TransmittableThreadLocal<String> transmittableThreadLocal = new TransmittableThreadLocal<>();

    /**
     * ThreadLocal  可以将值在当前线程保存和传递
     * 但是无法传递到子线程
     * */
    @Override
    public void singleThread() {
        // 主线程赋值
        threadLocal.set(Thread.currentThread().getName() + ":A");

        // 输出null
        Thread thread1 = new Thread(() -> System.out.println(threadLocal.get()));
        // 输出null
        Thread thread2 = new Thread(() -> System.out.println(threadLocal.get()));

        thread1.start();
        thread2.start();
        // 输出http-nio-15002-exec-4:A
        System.out.println(threadLocal.get());
    }

    /**
     * InheritableThreadLocal  可以将值从父线程传递给子线程
     */
    @Override
    public void childThread() {
        // 主线程赋值
        inheritableThreadLocal.set(Thread.currentThread().getName() + ":A");

        // 输出http-nio-15002-exec-1:A
        Thread thread1 = new Thread(() -> System.out.println(inheritableThreadLocal.get()));
        // 输出http-nio-15002-exec-1:A
        Thread thread2 = new Thread(() -> System.out.println(inheritableThreadLocal.get()));

        thread1.start();
        thread2.start();
        // 输出http-nio-15002-exec-1:A
        System.out.println(inheritableThreadLocal.get());
    }

    @Override
    public void poolThread() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

//        inheritableThreadLocal.set(Thread.currentThread().getName() + ":A");
//        executorService.execute(() -> System.out.println(inheritableThreadLocal.get()));
//        TimeUnit.SECONDS.sleep(1);
//        inheritableThreadLocal.set(Thread.currentThread().getName() + ":C");
//        executorService.execute(() -> System.out.println(inheritableThreadLocal.get()));

        ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(executorService);
        transmittableThreadLocal.set(Thread.currentThread().getName() + ":A");
        ttlExecutorService.execute(() -> System.out.println(transmittableThreadLocal.get()));
        TimeUnit.SECONDS.sleep(1);
        transmittableThreadLocal.set(Thread.currentThread().getName() + ":C");
        ttlExecutorService.execute(() -> System.out.println(transmittableThreadLocal.get()));
    }
}


