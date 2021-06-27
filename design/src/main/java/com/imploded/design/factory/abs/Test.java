package com.imploded.design.factory.abs;

/**
 * 抽象工厂模式
 *
 * 先获取对应的工厂， 再获取工厂内的大功能， 再再获取大功能的小功能
 *
 * 手机->短信和推送功能->短信的发送和推送的发送
 *
 * 可以增加新的厂商如  APNSFactory   APNSSms   APNSPush
 *
 * 可以增加厂商的通用功能如  IM  即增加 IM接口   增加oppoIm实现类   增加HuaWeiIm实现类   再在每个工厂内增加IM对象获取
 *
 * 每次增加新的业务还是要改动很大！！！， 要给工厂类加方法， 然后每个实现类都要去实现这个新的接口
 * */
public class Test {
    public static void main(String[] args) {
        HuaWeiFactory huaWeiFactory = new HuaWeiFactory();
        Push push = huaWeiFactory.getPush();
        push.push();


        OppoFactory oppoFactory = new OppoFactory();
        Sms sms = oppoFactory.getSms();
        sms.sendSms();

        Im im = oppoFactory.getIm();
        im.send();
    }
}
