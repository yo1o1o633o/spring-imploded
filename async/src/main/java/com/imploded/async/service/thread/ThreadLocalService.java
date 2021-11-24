package com.imploded.async.service.thread;

public interface ThreadLocalService {

    void singleThread();

    void childThread();

    void poolThread() throws InterruptedException;
}
