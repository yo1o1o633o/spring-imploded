package com.imploded.push.apns;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author shuai.yang
 */
public class ApnsPush {
    private ApnsPushListener listener;

    public ApnsPush(ApnsPushListener listener) {
        this.listener = listener;
    }

    public static void initApns(String env,Integer semaphoreValue,Integer nioEventLoopGroupCount,Integer connectChannelCount) throws IOException {

    }

    public void push(final List<String> deviceTokens, String alertTitle, String alertBody, List<Integer> badges, boolean contentAvailable, boolean mutableContent, Map<String, Object> customProperty, Integer awaitLevelTime, String collapseId, boolean watchDetail) {

    }
}
