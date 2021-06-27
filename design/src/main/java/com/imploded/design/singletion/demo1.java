package com.imploded.design.singletion;


/**
 * 单例模式， 饿汉模式
 *
 * 不是懒加载
 * JVM加载类时， 创建对象。
 * 会浪费内存
 *
 * 因为是在JVM加载时创建的对象， 所以不会出现线程安全问题
 * */
public class demo1 {
    public static void main(String[] args) {
        User user1 = User.getUser();
        User user2 = User.getUser();

    }
}
class User {
    // JVM加载类时， 创建对象
    private static final User user = new User();
    // 私有化构造器， 外部不可以new当前对象, 只能通过getUser获取
    private User() {

    }

    static User getUser() {
        return user;
    }
}

class User2 {
    private static User2 user;
    // 在静态代码块中创建对象
    static {
        user = new User2();
    }
    // 私有化构造器， 外部不可以new当前对象, 只能通过getUser获取
    private User2() {

    }

    static User2 getUser() {
        return user;
    }
}