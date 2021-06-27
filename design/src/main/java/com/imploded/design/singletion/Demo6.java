package com.imploded.design.singletion;

/**
 * 基于枚举类实现单例模式。 推荐
 * */
public class Demo6 {
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Singleton6 one = Singleton6.One;
                    System.out.println(one);
                }
            });
            thread.start();
        }
    }
}

enum Singleton6 {
    One;
}
