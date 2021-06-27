package com.imploded.design.factory.simple;

public class ApnsPush implements Push {
    @Override
    public void send() {
        System.out.println("Apple通道推送");
    }
}
