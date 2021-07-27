package com.imploded.push.apns;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * @author shuai.yang
 */
public class ApnsPush {
    private ApnsPushListener listener;

    private static Semaphore semaphore;

    public ApnsPush(ApnsPushListener listener) {
        this.listener = listener;
    }

    public static void initApns(String env,Integer semaphoreValue,Integer nioEventLoopGroupCount,Integer connectChannelCount) throws IOException {
        ApnsConnect.initApnsClient(env);
        ApnsConnect.concurrentConnectionCount = connectChannelCount;
        ApnsConnect.nioEventLoopGroupCount = nioEventLoopGroupCount;
        semaphore = new Semaphore(semaphoreValue);
    }

    public void push(final List<String> deviceTokens, String alertTitle, String alertBody, List<Integer> badges, boolean contentAvailable, boolean mutableContent, Map<String, Object> customProperty, Integer awaitLevelTime, String collapseId, boolean watchDetail) {
        final CountDownLatch latch = new CountDownLatch(deviceTokens.size());
        final ApnsClient apnsClient = ApnsConnect.getAPNSConnect();
    }
}
