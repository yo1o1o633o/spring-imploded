package com.imploded.complex.service.redis.impl;

import com.imploded.complex.service.redis.ValueToHashService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
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
        setBit();
    }

    private void setHash() {
        HashOperations<String, String, Integer> opsForHash = redisTemplate.opsForHash();

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 1000000; j++) {
                    String s = DigestUtils.md5Hex("A" + j + System.currentTimeMillis());
                    String cacheKey = generateCacheKey(s);
                    opsForHash.put(cacheKey,  "" + s.hashCode(), 1);
                    if (j % 1000 == 0) {
                        log.info("写入数据: {}, {}", Thread.currentThread().getName(), j);
                    }
                }
            });
            thread.start();
        }
    }

    private String generateCacheKey(String key) {
        CRC32 crc32 = new CRC32();
        crc32.update(key.getBytes());
        return crc32.getValue() % BUCKET_SIZE + "";
    }

    private void setBit() {
        redisTemplate.opsForValue().setBit("bit", Integer.MAX_VALUE / 2, true);
    }
}
