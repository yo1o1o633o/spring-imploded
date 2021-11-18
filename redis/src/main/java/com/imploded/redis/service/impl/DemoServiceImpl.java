package com.imploded.redis.service.impl;

import com.imploded.redis.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    RedisTemplate<String, Integer> redisTemplate;

    @Override
    public void test() {
        for (int i = 0; i < 10000; i++) {
            try {
                redisTemplate.opsForValue().set("A" + i, 1);
                System.out.println("write: " + i);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("err");
            }
        }
        System.out.println("成功!");
    }
}
