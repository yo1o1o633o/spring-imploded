package com.imploded.async.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Demo2 {
    public static void main(String[] args) {
        Worker worker = newWorker();

        Wrapper wrapper = new Wrapper();
        wrapper.setWorker(worker);
        wrapper.setParams("一个业务参数");
        // 监听器模式
        // 此处要确保setListener比任务的执行时间快, 即先setListener, 然后执行任务, 然后getListener才能拿到Listener
        wrapper.setListener(new Listener() {
            @Override
            public void result(Object result) {
                System.out.println("一个监听器, 返回值" + result);
            }
        });

        CompletableFuture future = CompletableFuture.supplyAsync(() -> doWorker(wrapper));

        try {
            future.get(800, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            wrapper.getListener().result("超时");
        }
        System.out.println(Thread.currentThread().getName());
    }

    private static Wrapper doWorker(Wrapper wrapper) {
        Worker worker = wrapper.getWorker();
        String result = worker.action(wrapper.getParams());

        wrapper.getListener().result(result);
        return wrapper;
    }

    private static Worker newWorker() {
        return new Worker() {
            @Override
            public String action(Object object) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "一个任务";
            }
        };
    }
}
