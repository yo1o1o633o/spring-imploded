package com.imploded.design.factory.simple;

public class PushFactory {

    public static Push getPush(String channel) {
        if (channel.equals("OPPO")) {
            return new OppoPush();
        } else if (channel.equals("APNS")) {
            return new ApnsPush();
        }
        return null;
    }
}
