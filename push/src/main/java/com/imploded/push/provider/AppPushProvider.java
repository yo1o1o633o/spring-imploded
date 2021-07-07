package com.imploded.push.provider;

import com.imploded.push.dto.RedirectAppMsgDTO;
import com.imploded.push.entity.DeviceToken;

import java.util.List;

/**
 * @author shuai.yang
 */
public interface AppPushProvider {
    void groupPush(String bizMsgId, String messageId, RedirectAppMsgDTO reqDTO, List<DeviceToken> tokens);
}
