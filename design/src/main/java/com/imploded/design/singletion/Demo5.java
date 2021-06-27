package com.imploded.design.singletion;

/**
 * 静态内部类的单例模式， 基于JVM来保证线程安全
 *
 * JVM装载类时线程安全的，
 * 同时解决懒加载, 外部类加载时不会装载内部类， 当调用内部类时，才会装载
 * 静态内部类在JVM装载时是线程安全的
 *
 */
public class Demo5 {
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Singleton5 singleton = Singleton5.getInstance();
                    System.out.println(singleton);
                }
            });
            thread.start();
        }
    }

}

class Singleton5 {
    private static class SingletonInstance {
        private static final Singleton5 singleton = new Singleton5();
    }
    public static Singleton5 getInstance() {
        return SingletonInstance.singleton;
    }
}

