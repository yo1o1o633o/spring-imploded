package com.imploded.redis.service.impl;

import com.imploded.redis.service.ValueToHashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.zip.CRC32;

/**
 * @author shuai.yang
 */
@Slf4j
@Service
public class ValueToHashServiceImpl implements ValueToHashService {
    @Autowired
    RedisTemplate<String, Integer> redisTemplate;

    private static final Integer BUCKET_SIZE = 30000;

    @Override
    public void makeValueData() {
        for (int i = 0; i < 10000000; i++) {
            redisTemplate.opsForValue().set("BAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + i, 1);
            log.info("写入完成: {}", i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String generateCacheKey(String key) {
        CRC32 crc32 = new CRC32();
        crc32.update(key.getBytes());
        return crc32.getValue() % BUCKET_SIZE + "";
    }
}
