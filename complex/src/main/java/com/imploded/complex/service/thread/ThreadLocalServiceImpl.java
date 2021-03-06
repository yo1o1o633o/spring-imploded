package com.imploded.complex.service.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ThreadLocalServiceImpl implements ThreadLocalService {

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    @Override
    public void test() {
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(i);
            user.setName("A");
            taskExecutor.submit(() -> operation(user));
        }
    }

    private void operation(User user) {
        threadLocal.set(user);
        operationId();
        operationName();
    }

    private void operationId() {
        User user = threadLocal.get();
        System.out.println("处理ID" + user.getId());
    }

    private void operationName() {
        User user = threadLocal.get();
        System.out.println("处理name" + user.getName());
    }
}


