package com.imploded.complex.controller;

import com.imploded.complex.service.redis.ValueToHashService;
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

    @RequestMapping("/make/value")
    public void makeValueData() {
        valueToHashService.makeValueData();
    }
}
