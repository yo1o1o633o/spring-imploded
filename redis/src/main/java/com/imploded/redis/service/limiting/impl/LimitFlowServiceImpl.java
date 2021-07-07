package com.imploded.redis.service.limiting.impl;

import com.imploded.redis.service.limiting.LimitFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;
import java.util.UUID;

/**
 * 基于redis实现的限流
 * */
public class LimitFlowServiceImpl implements LimitFlowService {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    private Integer num = 100;
    // 1 每分钟  2 每小时   3 每秒
    private Integer timeUnit = 1;

    /**
     * 简单版限流。 使用key的有效时间
     *
     * 弊端是没有时间窗口， 无法限制连续的高并发。 在每个失效的时间节点不准确
     * */
    public boolean limitFlowWhitKey() {
        String key = redisTemplate.opsForValue().get("key");
        if (key != null && Integer.parseInt(key) > 100) {
            return true;
        }
        redisTemplate.opsForValue().increment("key");
        return false;
    }

    /**
     * 利用有序集合
     * 分数为时间戳
     * 元素为唯一ID
     *
     * 原理是判断分数区间的元素个数是否超出限制
     *
     * 弊端是占用空间。
     * */
    public boolean limitFlow() {
        long now = System.currentTimeMillis() / 1000;
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        long tar = 0;
        if (timeUnit == 1) {
            tar = now - 60;
        } else if (timeUnit == 2) {
            tar = now - 3600;
        } else {
            tar = now - 1;
        }
        Set<ZSetOperations.TypedTuple<String>> scores = zSetOperations.rangeWithScores("key", now, tar);
        if (scores != null && scores.size() > num) {
            return true;
        }
        zSetOperations.add("key", UUID.randomUUID().toString(), now);
        return false;
    }

    /**
     * 获取到令牌， 即正常执行， 否则限流
     * */
    private boolean limitFlowWithToken() {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        String token = listOperations.leftPop("key");
        if (token == null) {
            return true;
        }
        return false;
    }
    /**
     * 定时任务， 每秒向桶内添加令牌
     * 这一步可以使用lua脚本嵌入到redis中执行。 保证原子性同时不需要网络请求开销
     * */
    private void addToken() {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.rightPush("key", UUID.randomUUID().toString());
    }
}
