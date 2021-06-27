package com.imploded.design.singletion;

/**
 * 懒汉式单例模式(保证线程安全)
 *
 * 当使用时调用获取对象方法， 如果是第一次调用则创建， 后续调用直接获取
 *
 */
public class Demo3 {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Singleton2 singleton = Singleton2.getInstance();
                    System.out.println(singleton);
                }
            });
            thread.start();
        }
        // synchronized保证线程安全, 加锁, 但是锁的粒度大
    }
}

class Singleton2 {
    private static Singleton2 singleton;

    private Singleton2() {

    }
    // synchronized 增加锁, 可以理解为当前方法在多线程操作时变成了串行
    public static synchronized Singleton2 getInstance() {
        // 多线程操作时，singleton == null 无法保证两个线程的创建先后
        if (singleton == null) {
            singleton = new Singleton2();
        }
        return singleton;
    }
}
