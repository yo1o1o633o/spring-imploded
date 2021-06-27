package com.imploded.design.factory.method;

/**
 * 方法工厂模式
 *
 * 给每个功能类加一个功能类工厂， 通过特定工厂获取对应的对象
 *
 * 每增加一种功能， 都要增加一个工厂
 * */
public class Test {
    public static void main(String[] args) {
        Push apnsPush = new ApnsPushFactory().getPush();
        Push oppoPush = new OppoPushFactory().getPush();

        apnsPush.send();
        oppoPush.send();
    }
}
