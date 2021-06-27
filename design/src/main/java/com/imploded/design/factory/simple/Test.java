package com.imploded.design.factory.simple;

/**
 * 简单工厂模式
 *
 * 就是将创建对象放到一个工厂类里，类提供一个方法根据传参获取对应的对象
 * */
public class Test {
    public static void main(String[] args) {
        Push oppoPush = PushFactory.getPush("OPPO");
        Push apnsPush = PushFactory.getPush("APNS");

        apnsPush.send();
        oppoPush.send();
    }
}
