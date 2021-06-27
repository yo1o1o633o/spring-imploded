package com.imploded.design.factory.abs;

public class OppoFactory implements ChannelFactory {
    @Override
    public Sms getSms() {
        return new OppoSms();
    }

    @Override
    public Push getPush() {
        return new OppoPush();
    }

    @Override
    public Im getIm() {
        return new OppoIm();
    }
}
