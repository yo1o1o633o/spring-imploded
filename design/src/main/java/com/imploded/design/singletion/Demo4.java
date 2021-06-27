package com.imploded.design.singletion;

/**
 * 懒汉式单例模式(保证线程安全)
 *
 * 解决为保证线程安全而加锁导致的性能问题
 * 双重校验
 * 当使用时调用获取对象方法， 如果是第一次调用则创建， 后续调用直接获取
 *
 */
public class Demo4 {
    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Singleton3 singleton = Singleton3.getInstance();
                    System.out.println(singleton);
                }
            });
            thread.start();
        }
        // synchronized保证线程安全, 加锁, 但是锁的粒度大
    }
}

class Singleton3 {
    // volatile保证变量线程可见性
    private static volatile Singleton3 singleton;

    private Singleton3() {

    }
    public static Singleton3 getInstance() {
        // 此处null用来判断对象是否存在
        if (singleton == null) {
            // 此处锁用于处理, 多线程并发时导致的上个if判断不及时
            synchronized (Singleton3.class) {
                // 再次判断, 此处在锁内, 表明此处的操作对于多线程来说是串行执行的, 判断一定是有效的
                if (singleton == null) {
                    // 创建对象, 同时当前锁释放后上个if判断已经有值了
                    singleton = new Singleton3();
                }
            }
        }
        return singleton;
    }
}
