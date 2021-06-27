package com.imploded.design.factory.method;

public class ApnsPush implements Push {
    @Override
    public void send() {
        System.out.println("Apple通道推送");
    }
}
