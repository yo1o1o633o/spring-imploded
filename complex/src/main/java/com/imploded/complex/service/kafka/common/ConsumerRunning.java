package com.imploded.complex.service.kafka.common;

/**
 * @author shuai.yang
 */
public class ConsumerRunning {

    private static Boolean running = true;

    public static Boolean getRunning() {
        return running;
    }

    public static void setRunning(Boolean running) {
        ConsumerRunning.running = running;
    }
}
