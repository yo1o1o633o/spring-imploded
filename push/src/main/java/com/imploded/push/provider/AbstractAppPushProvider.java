package com.imploded.push.provider;

import com.imploded.push.entity.DeviceToken;

import java.util.List;
import java.util.Map;

/**
 * @author shuai.yang
 */
public abstract class AbstractAppPushProvider {
    protected void splitDeviceTokens(List<DeviceToken> deviceTokenList, List<String> firstIds, List<Integer> uids, Map<Integer, Integer> ids, List<String> provideDeviceTokens, List<Integer> badges) {
        DeviceToken deviceToken;
        for (int i = 0; i < deviceTokenList.size(); i++) {
            deviceToken = deviceTokenList.get(i);
            firstIds.add(deviceToken.getFirstId());
            uids.add(deviceToken.getUid());
            ids.put(i, deviceToken.getId());
            provideDeviceTokens.add(deviceToken.getProvideDeviceToken());
            badges.add(deviceToken.getBadge());
        }
    }

    protected String subMessage(String msg, Integer length) {
        if (msg.length() <= length) {
            return msg;
        } else {
            return msg.substring(0, length);
        }
    }
}
