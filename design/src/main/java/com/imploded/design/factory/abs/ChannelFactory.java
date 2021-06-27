package com.imploded.design.factory.abs;

public interface ChannelFactory {
    Sms getSms();

    Push getPush();

    Im getIm();
}
