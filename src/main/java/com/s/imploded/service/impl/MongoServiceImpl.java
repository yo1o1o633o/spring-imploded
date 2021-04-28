package com.s.imploded.service.impl;

import com.s.imploded.entity.mongo.Test;
import com.s.imploded.service.MongoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author shuai.yang
 */
@Slf4j
@Service
public class MongoServiceImpl implements MongoService {
    @Autowired
    MongoRepository mongoRepository;

    @Override
    public void insert() {
        long l = System.currentTimeMillis();
        for (int j = 0; j < 5000000; j++) {
            Test test = new Test();
            Random random = new Random();
            int i1 = random.nextInt(99999999);
            test.setPhoneId(i1);
            mongoRepository.insert(test);
            log.info("写入数据:{}", j);
        }
        log.info("执行时间:{}", System.currentTimeMillis() - l);
    }
}
