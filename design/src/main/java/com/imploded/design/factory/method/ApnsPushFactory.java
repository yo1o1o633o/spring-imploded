package com.imploded.design.factory.method;

public class ApnsPushFactory implements PushFactory {
    @Override
    public Push getPush() {
        return new ApnsPush();
    }
}
