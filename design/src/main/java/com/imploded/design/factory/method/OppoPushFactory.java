package com.imploded.design.factory.method;

public class OppoPushFactory implements PushFactory {
    @Override
    public Push getPush() {
        return new OppoPush();
    }
}
