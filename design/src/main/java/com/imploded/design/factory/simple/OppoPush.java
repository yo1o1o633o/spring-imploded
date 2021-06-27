package com.imploded.design.factory.simple;

public class OppoPush implements Push {
    @Override
    public void send() {
        System.out.println("oppo通道推送");
    }
}
