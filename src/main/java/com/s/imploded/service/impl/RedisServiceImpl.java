package com.s.imploded.service.impl;

import com.s.imploded.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author shuai.yang
 */
public class RedisServiceImpl implements RedisService {
    @Autowired
    RedisTemplate<String, Integer> stringIntegerRedisTemplate;
    @Autowired
    RedisTemplate<String, String> setRedisTemplate;
    @Autowired
    RedisTemplate<String, String> listRedisTemplate;

    public void string() {
        ValueOperations<String, Integer> stringOperations = stringIntegerRedisTemplate.opsForValue();
        stringOperations.set("demo1", 1);
        stringOperations.set("demo2", 2, 1000, TimeUnit.SECONDS);
        stringOperations.set("demo3", 3, 1000);
        stringOperations.set("demo4", 4, Duration.ZERO);

        stringOperations.increment("demo1", 1);
        stringOperations.increment("demo1");
    }

    public void set() {
        SetOperations<String, String> setOperations = setRedisTemplate.opsForSet();

        Long add = setOperations.add("setDemo1", "aaa");
        // 取出指定集合KEY中的元素
        Set<String> setDemo1 = setOperations.members("setDemo1");

        Boolean isMember = setOperations.isMember("setDemo1", "aaa");
        if (isMember == null || !isMember) {
            System.out.println("集合中不存在数据");
        } else {
            System.out.println("集合中存在数据");
        }
        // 指定集合中元素个数
        Long size = setOperations.size("setDemo1");
    }

    public void list() {
        ListOperations<String, String> listOperations = listRedisTemplate.opsForList();


    }
}
