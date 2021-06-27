package com.imploded.design.factory.abs;

public class HuaWeiFactory implements ChannelFactory {
    @Override
    public Sms getSms() {
        return new HuaWeiSms();
    }

    @Override
    public Push getPush() {
        return new HuaWeiPush();
    }

    @Override
    public Im getIm() {
        return new OppoIm();
    }
}
