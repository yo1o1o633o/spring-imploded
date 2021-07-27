package com.imploded.push.provider.impl;

import com.imploded.push.apns.ApnsPush;
import com.imploded.push.apns.ApnsPushListener;
import com.imploded.push.dto.PushMessageDTO;
import com.imploded.push.dto.RedirectAppMsgDTO;
import com.imploded.push.entity.DeviceToken;
import com.imploded.push.provider.AbstractAppPushProvider;
import com.imploded.push.provider.AppPushProvider;
import com.imploded.push.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shuai.yang
 */
@Slf4j
public class ApnsAppPushProvider extends AbstractAppPushProvider implements AppPushProvider {
    @Value("${app.push.apns.credentials.env:release}")
    private String credentialsEnv;
    @Value("${app.push.apns.group.size.max:1000}")
    private Integer maxGroupSize;
    @Value("${app.push.apns.await.levels:5,10,20,40,60}")
    private int[] awaitLevels;
    @Value("${app.push.apns.nio.event.loop.count:8}")
    private int nioEventLoopGroupCount;
    @Value("${app.push.apns.nio.socket.channel.count:8}")
    private int connectChannelCount;
    @Value("#{'${app.push.apns.user.invalid.desc:Unregistered,BadDeviceToken,InvalidProviderToken}'.split(',')}")
    List<String> invalidUserDescList;
    @Value("${app.push.apns.watch.detail:true}")
    public boolean watchDetail;

    public void init() throws Exception {
        try {
            ApnsPush.initApns(credentialsEnv, maxGroupSize, nioEventLoopGroupCount, connectChannelCount);
        } catch (IOException e) {
            log.error("apns:appPush初始化证书环境失败！", e);
            throw new Exception("", e);
        }
    }

    @Override
    public void groupPush(String bizMsgId, String messageId, RedirectAppMsgDTO reqDTO, List<DeviceToken> tokens) {
        List<String> firstIds = new ArrayList<>(tokens.size());
        List<Integer> uids = new ArrayList<>(tokens.size());
        List<String> invalidFirstIds = new ArrayList<>(tokens.size());
        //map存储下标以及对应id
        Map<Integer, Integer> ids = new HashMap<>(tokens.size());
        List<Integer> rmIds = new ArrayList<>();
        List<String> provideDeviceTokens = new ArrayList<>(tokens.size());
        List<Integer> badges = new ArrayList<>(tokens.size());
        int pushTimestamp = TimeUtils.getCurrentTimestamp();
        long startTime = System.currentTimeMillis();

        this.splitDeviceTokens(tokens, firstIds, uids, ids, provideDeviceTokens, badges);

        push(messageId, reqDTO, provideDeviceTokens, badges);
    }

    private void push(String messageId, RedirectAppMsgDTO reqDTO, List<String> provideDeviceTokens, List<Integer> badges) {
        MyApnsPushListener myApnsPushListener = new MyApnsPushListener();

        ApnsPush iosPush = new ApnsPush(myApnsPushListener);
        PushMessageDTO message = reqDTO.getMessage();
        String title = message.getTitle();
        String description = subMessage(message.getDescription(), 100);

        iosPush.push(provideDeviceTokens, title, description, badges, true, true, null, null, null, true);
    }

    private class MyApnsPushListener extends ApnsPushListener {

    }
}
