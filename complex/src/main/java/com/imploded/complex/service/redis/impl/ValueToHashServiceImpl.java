package com.imploded.complex.service.redis.impl;

import com.imploded.complex.service.redis.ValueToHashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author shuai.yang
 */
@Slf4j
@Service
public class ValueToHashServiceImpl implements ValueToHashService {
    @Autowired
    RedisTemplate<String, Integer> redisTemplate;

    @Override
    public void makeValueData() {
        for (int i = 0; i < 10000000; i++) {
            redisTemplate.opsForValue().set("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + i, 1);
            if (i % 1000 == 0) {
                log.info("写入完成: {}", i);
            }
        }
    }
}
