package com.imploded.design.singletion;

/**
 * 懒汉式单例模式
 *
 * 不能保证线程安全
 *
 * 当使用时调用获取对象方法， 如果是第一次调用则创建， 后续调用直接获取
 *
 */
public class Demo2 {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Singleton singleton = Singleton.getInstance();
                System.out.println(singleton);
            }
        });
        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Singleton singleton = Singleton.getInstance();
                System.out.println(singleton);
            }
        });
        thread2.start();

        // com.imploded.design.singletion.Singleton@2f8f6cde
        // com.imploded.design.singletion.Singleton@23272363
        // 两个线程获取到了两个对象, 单例失效
        // 两个线程同时请求, 第一个线程创建对象后赋值未完成前, 线程2请求判断为空,也会进行创建
        // 在两个线程中间加个等待时间, 则会是同一个对象
    }
}

class Singleton {
    private static Singleton singleton;

    private Singleton() {

    }

    public static Singleton getInstance() {
        // 多线程操作时，singleton == null 无法保证两个线程的创建先后
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }
}
