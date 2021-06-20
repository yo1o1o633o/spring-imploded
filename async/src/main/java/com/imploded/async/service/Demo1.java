package com.imploded.async.service;

import java.util.concurrent.*;

public class Demo1 {
    /**
     * 带回调的异步线程调用
     * 就是创建一个带返回值的异步任务， 丢到线程池里去执行。  通过get阻塞等待异步任务的返回结果
     * */
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<Integer> submit = executorService.submit(new Task());

        try {
            // get会阻塞等待返回值
            System.out.println(submit.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class Task implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {

            Thread.sleep(3000);
            return 1;
        }
    }
}
