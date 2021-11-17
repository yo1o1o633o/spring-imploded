package com.imploded.redis.controller;

import com.imploded.redis.service.DemoService;
import com.imploded.redis.service.ValueToHashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuai.yang
 */
@RestController
public class RedisController {
    @Autowired
    ValueToHashService valueToHashService;

    @Autowired
    DemoService demoService;

    @RequestMapping("/redis/test")
    public void testRedis() {
        demoService.test();
    }

    @RequestMapping("/make/value")
    public void makeValueData() {
        valueToHashService.makeValueData();
    }
}
